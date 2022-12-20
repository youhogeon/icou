package com.youhogeon.icou.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youhogeon.icou.dto.AccountCreateRequestDto;
import com.youhogeon.icou.dto.AccountJwtTokenReissueRequestDto;
import com.youhogeon.icou.dto.AccountSigninRequestDto;
import com.youhogeon.icou.dto.JwtTokenResponseDto;
import com.youhogeon.icou.error.InvalidTokenException;
import com.youhogeon.icou.model.Account;
import com.youhogeon.icou.model.RefreshToken;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.repository.RefreshTokenRepository;
import com.youhogeon.icou.security.JwtTokenProvider;
import com.youhogeon.icou.util.SecurityUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Transactional
    public void create(AccountCreateRequestDto accountCreateRequestDto) {
        Account account = Account.from(accountCreateRequestDto, passwordEncoder);

        List<Account> duplicatedEmail = accountRepository.findByEmail(account.getEmail());

        if (!duplicatedEmail.isEmpty()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        accountRepository.save(account);
    }

    @Transactional
    public JwtTokenResponseDto signIn(AccountSigninRequestDto memberRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        JwtTokenResponseDto tokenDto = tokenProvider.generateJwtTokenResponseDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(Long.parseLong(authentication.getName()))
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public JwtTokenResponseDto reissue(AccountJwtTokenReissueRequestDto requestDto) {
        Authentication authentication = tokenProvider.getAuthentication(requestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(Long.parseLong(authentication.getName()))
            .orElseThrow(() -> new InvalidTokenException("잘못된 갱신 토큰입니다."));

        if (!refreshToken.getValue().equals(requestDto.getRefreshToken())) {
            throw new InvalidTokenException("잘못된 갱신 토큰입니다.");
        }

        JwtTokenResponseDto tokenDto = tokenProvider.generateJwtTokenResponseDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    public String signOut() {
        Long currentAccountId = SecurityUtil.getCurrentAccountId();
        
        try{
            refreshTokenRepository.deleteById(currentAccountId);
        } catch (EmptyResultDataAccessException e) {

        }

        return null;
    }

}
