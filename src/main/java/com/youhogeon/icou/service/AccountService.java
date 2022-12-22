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

import com.youhogeon.icou.controller.dto.request.AccountCreateRequestDto;
import com.youhogeon.icou.controller.dto.request.AccountJwtTokenReissueRequestDto;
import com.youhogeon.icou.controller.dto.request.AccountSigninRequestDto;
import com.youhogeon.icou.controller.dto.response.JwtTokenResponseDto;
import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.domain.RefreshToken;
import com.youhogeon.icou.error.BusinessException;
import com.youhogeon.icou.error.ErrorCode;
import com.youhogeon.icou.error.InvalidTokenException;
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
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
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
            throw new BusinessException(ErrorCode.BAD_CREDENTIALS);
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
            .orElseThrow(() -> new InvalidTokenException());

        if (!refreshToken.getValue().equals(requestDto.getRefreshToken())) {
            throw new InvalidTokenException();
        }

        JwtTokenResponseDto tokenDto = tokenProvider.generateJwtTokenResponseDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    public void signOut() {
        Long currentAccountId = SecurityUtil.getCurrentAccountId();
        
        try{
            refreshTokenRepository.deleteById(currentAccountId);
        } catch (EmptyResultDataAccessException e) {
            //DO NOTHING
        }
    }

}
