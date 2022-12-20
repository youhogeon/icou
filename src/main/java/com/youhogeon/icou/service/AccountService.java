package com.youhogeon.icou.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youhogeon.icou.dto.AccountCreateRequestDto;
import com.youhogeon.icou.model.Account;
import com.youhogeon.icou.repository.AccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    
    @Transactional
    public void create(AccountCreateRequestDto accountCreateRequestDto) {
        Account account = Account.from(accountCreateRequestDto);

        List<Account> duplicatedEmail = accountRepository.findByEmail(account.getEmail());

        if (!duplicatedEmail.isEmpty()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        accountRepository.save(account);
    }

}
