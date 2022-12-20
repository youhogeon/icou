package com.youhogeon.icou.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youhogeon.icou.dto.AccountCreateRequestDto;
import com.youhogeon.icou.dto.AccountSigninRequestDto;
import com.youhogeon.icou.dto.JwtTokenResponseDto;
import com.youhogeon.icou.model.Account;
import com.youhogeon.icou.model.RefreshToken;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.repository.RefreshTokenRepository;
import com.youhogeon.icou.security.JwtTokenProvider;

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

    public JwtTokenResponseDto signin(AccountSigninRequestDto memberRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtTokenResponseDto tokenDto = tokenProvider.generateJwtTokenResponseDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

}
