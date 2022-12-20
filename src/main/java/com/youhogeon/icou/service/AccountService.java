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
        
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtTokenResponseDto tokenDto = tokenProvider.generateJwtTokenResponseDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

}
