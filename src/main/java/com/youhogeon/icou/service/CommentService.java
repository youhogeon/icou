package com.youhogeon.icou.service;

import org.springframework.stereotype.Service;

import com.youhogeon.icou.domain.Account;
import com.youhogeon.icou.domain.Comment;
import com.youhogeon.icou.domain.Resource;
import com.youhogeon.icou.dto.CommentCreateRequestDto;
import com.youhogeon.icou.error.BusinessException;
import com.youhogeon.icou.error.ErrorCode;
import com.youhogeon.icou.repository.AccountRepository;
import com.youhogeon.icou.repository.CommentRepository;
import com.youhogeon.icou.repository.ResourceRepository;
import com.youhogeon.icou.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final ResourceRepository resourceRepository;

    public void create(String token, CommentCreateRequestDto commentRequestDto) {
        Long currentAccountId = SecurityUtil.getCurrentAccountId();
        Account currentAccount = accountRepository.findById(currentAccountId).get();

        Resource resource = resourceRepository.findByToken(token).orElseThrow(
            () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        Comment comment = Comment.builder()
            .account(currentAccount)
            .resource(resource)
            .comment(commentRequestDto.getComment())
            .build();

        commentRepository.save(comment);
    }
    
}
