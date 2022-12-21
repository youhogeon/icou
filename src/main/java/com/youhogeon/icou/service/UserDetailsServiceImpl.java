package com.youhogeon.icou.service;

import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.error.InvalidTokenException;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.security.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Account> accounts = accountRepository.findByEmail(username);

        if (accounts.isEmpty()) throw new InvalidTokenException();

        return createUserDetails(accounts.get(0));
    }

    private UserDetails createUserDetails(Account account) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(Role.User.toString());

        return new User(
            String.valueOf(account.getId()),
            account.getPassword(),
            Collections.singleton(grantedAuthority)
        );
    }
}