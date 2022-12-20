package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.youhogeon.icou.dto.AccountCreateRequestDto;
import com.youhogeon.icou.service.AccountService;
import com.youhogeon.icou.util.ResponseUtil.Response;

import static com.youhogeon.icou.util.ResponseUtil.success;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class AccountController {

    private final AccountService accountService;
    
    @PostMapping("")
    public Response<?> createAccount(@Validated AccountCreateRequestDto accountCreateRequestDto) {
        accountService.create(accountCreateRequestDto);

        return success();
    }

}
