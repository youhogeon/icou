package com.youhogeon.icou.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.youhogeon.icou.controller.dto.response.JwtTokenResponseDto;
import com.youhogeon.icou.error.InvalidTokenException;
import com.youhogeon.icou.repository.AccountRepository;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String AUTHORITIES_KEY = "auth";
    private final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;

    private final AccountRepository accountRepository;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expire-time:#{1000 * 60 * 30}}") long accessTokenExpireTime,
        @Value("${jwt.refresh-token-expire-time:#{1000 * 60 * 30}}") long refreshTokenExpireTime,
        AccountRepository accountRepository
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
        this.accountRepository = accountRepository;
    }

    public JwtTokenResponseDto generateJwtTokenResponseDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();


        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
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
            validateAccount(Long.parseLong(claims.getSubject()));

        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");

            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");

            throw new InvalidTokenException();
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰입니다.");

            throw new InvalidTokenException();
        }

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new InvalidTokenException();
        }

        return claims;
    }

    private void validateAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            log.error("존재하지 않는 사용자의 JWT 토큰입니다.");

            throw new InvalidTokenException();
        }
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }
}
