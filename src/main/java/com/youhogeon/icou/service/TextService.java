package com.youhogeon.icou.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youhogeon.icou.controller.dto.request.TextCreateRequestDto;
import com.youhogeon.icou.controller.dto.response.TextCreateResponseDto;
import com.youhogeon.icou.controller.dto.response.TextResponseDto;
import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.domain.Resource;
import com.youhogeon.icou.domain.ResourceType;
import com.youhogeon.icou.error.BusinessException;
import com.youhogeon.icou.error.ErrorCode;
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
    public TextCreateResponseDto create(TextCreateRequestDto textRequestDto) {
        String key = generateKey();

        Timestamp expiredAt = new Timestamp((new Date()).getTime() + 1000 * 60 * 60 * 24 * 7);

        Resource resource = Resource.builder()
            .token(key)
            .type(ResourceType.TEXT)
            .data(textRequestDto.getText())
            .expiredAt(expiredAt)
            .build();

            
        Long currentAccountId = SecurityUtil.getCurrentAccountId();
        if (currentAccountId != null) {
            Account account = accountRepository.findById(currentAccountId).get();
            resource.setAccount(account);
        }

        resourceRepository.save(resource);
        
        TextCreateResponseDto textResponseDto = TextCreateResponseDto.builder()
            .key(key)
            .build();
        
        return textResponseDto;
    }

    public TextResponseDto get(String token) {
        Resource resource = resourceRepository.findByToken(token).orElseThrow(
            () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        long expiredAfterSeconds = (resource.getExpiredAt().getTime() - (new Date()).getTime()) / 1000;

        TextResponseDto textResponseDto = TextResponseDto.builder()
            .text(resource.getData())
            .expiredAfterSeconds(expiredAfterSeconds)
            .build();

        return textResponseDto;
    }
    
}
