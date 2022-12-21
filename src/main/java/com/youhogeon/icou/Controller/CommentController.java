package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youhogeon.icou.dto.CommentCreateRequestDto;
import com.youhogeon.icou.dto.ResponseDto;
import com.youhogeon.icou.service.CommentService;

import lombok.RequiredArgsConstructor;

import static com.youhogeon.icou.util.ResponseUtil.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resources/{type}/{token}/comments")
public class CommentController {
    
    private final CommentService commentService;

    @PostMapping("")
    public ResponseDto<?> get(@PathVariable("token") String token, @Validated @RequestBody CommentCreateRequestDto commentRequestDto) {
        commentService.create(token, commentRequestDto);

        return ok();
    }
    
}
