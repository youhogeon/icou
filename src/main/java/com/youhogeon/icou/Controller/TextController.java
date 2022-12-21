package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youhogeon.icou.dto.ResponseDto;
import com.youhogeon.icou.dto.TextRequestDto;
import com.youhogeon.icou.dto.TextResponseDto;
import com.youhogeon.icou.service.TextService;

import lombok.RequiredArgsConstructor;

import static com.youhogeon.icou.util.ResponseUtil.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources/texts")
public class TextController {

    private final TextService textService;

    @PostMapping("")
    public ResponseDto<?> create(@Validated @RequestBody TextRequestDto textRequestDto) {
        TextResponseDto textResponseDto = textService.create(textRequestDto);

        return ok(textResponseDto);
    }

}
