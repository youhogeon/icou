package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youhogeon.icou.controller.dto.request.CommentCreateRequestDto;
import com.youhogeon.icou.controller.dto.response.CommentResponseDto;
import com.youhogeon.icou.controller.dto.response.ResponseDto;
import com.youhogeon.icou.service.CommentService;

import lombok.RequiredArgsConstructor;

import static com.youhogeon.icou.util.ResponseUtil.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resources/{type}/{token}/comments")
public class CommentController {
    
    private final CommentService commentService;

    @GetMapping(value = {"{page}", ""})
    public ResponseDto<?> get(@PathVariable String token, @PathVariable(required = false) Integer page) {
        if (page == null) page = 1;

        CommentResponseDto commentResponseDto = commentService.get(token, page);

        return ok(commentResponseDto);
    }

    @PostMapping("")
    public ResponseDto<?> create(@PathVariable String token, @Validated @RequestBody CommentCreateRequestDto commentRequestDto) {
        commentService.create(token, commentRequestDto);

        return ok();
    }

}
