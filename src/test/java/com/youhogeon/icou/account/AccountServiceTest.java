package com.youhogeon.icou.account;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.youhogeon.icou.controller.dto.request.AccountCreateRequestDto;
import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.error.BusinessException;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.repository.RefreshTokenRepository;
import com.youhogeon.icou.security.JwtTokenProvider;
import com.youhogeon.icou.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private Account account;
    private List<Account> accountList;
    AccountCreateRequestDto accountCreateRequestDto;

    @BeforeEach
    void setUp() {
        accountCreateRequestDto = AccountCreateRequestDto.builder()
            .email("test@test.com")
            .nickname("test")
            .password("1234")
            .build();

        account = Account.from(accountCreateRequestDto, passwordEncoder);

        accountList = new ArrayList<>();
        accountList.add(account);

        lenient().when(accountRepository.findByEmail("test@test.com")).thenReturn(accountList);
        lenient().when(accountRepository.findByEmail("test2@test.com")).thenReturn(new ArrayList<>());
    }

    @Test
    void 회원가입_성공() {
        AccountCreateRequestDto accountCreateRequestDto2 = AccountCreateRequestDto.builder()
            .email("test2@test.com")
            .nickname("test")
            .password("1234")
            .build();

        assertDoesNotThrow(() -> {
            accountService.create(accountCreateRequestDto2);
        });
    }

    @Test
    void 회원가입_실패_중복이메일() {
        assertThrows(BusinessException.class, () -> {
            accountService.create(accountCreateRequestDto);
        });
    }

}
