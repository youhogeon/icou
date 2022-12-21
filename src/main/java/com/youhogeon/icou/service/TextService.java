package com.youhogeon.icou.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.domain.Resource;
import com.youhogeon.icou.domain.ResourceType;
import com.youhogeon.icou.dto.TextRequestDto;
import com.youhogeon.icou.dto.TextResponseDto;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.repository.ResourceRepository;
import com.youhogeon.icou.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextService extends ResourceService {

    private final AccountRepository accountRepository;
    private final ResourceRepository resourceRepository;

    @Transactional
    public TextResponseDto create(TextRequestDto textRequestDto) {
        Long currentAccountId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(currentAccountId).get();

        String key = generateKey();

        Resource resource = Resource.builder()
            .account(account)
            .token(key)
            .type(ResourceType.TEXT)
            .data(textRequestDto.getText())
            .build();

        resourceRepository.save(resource);
        
        TextResponseDto textResponseDto = TextResponseDto.builder()
            .key(key)
            .build();
        
        return textResponseDto;
    }
    
}
