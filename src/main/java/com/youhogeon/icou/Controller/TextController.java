package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youhogeon.icou.dto.ResponseDto;
import com.youhogeon.icou.dto.TextRequestDto;

@RestController
@RequestMapping("/resources/texts")
public class TextController {

    public ResponseDto<?> create(@Validated @RequestBody TextRequestDto textRequestDto) {
        

        return null;
    }

}
