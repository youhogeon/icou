package com.youhogeon.icou.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.youhogeon.icou.dto.JwtTokenResponseDto;
import com.youhogeon.icou.error.InvalidTokenException;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String AUTHORITIES_KEY = "auth";
    private final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expire-time:#{1000 * 60 * 30}}") long accessTokenExpireTime,
        @Value("${jwt.refresh-token-expire-time:#{1000 * 60 * 30}}") long refreshTokenExpireTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
    }

    public JwtTokenResponseDto generateJwtTokenResponseDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();


        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = validateToken(accessToken);

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims validateToken(String accessToken) {
        Claims claims;

        try {
            claims = parseClaims(accessToken);

        } catch (SecurityException | MalformedJwtException e) { //서명 불일치
            throw new InvalidTokenException("잘못된 인증 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 인증 토큰입니다.");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("잘못된 인증 토큰입니다.");
        }

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new InvalidTokenException("권한 정보가 없는 토큰입니다.");
        }

        return claims;
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }
}
