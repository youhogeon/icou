package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.youhogeon.icou.dto.AccountCreateRequestDto;
import com.youhogeon.icou.dto.AccountJwtTokenReissueRequestDto;
import com.youhogeon.icou.dto.AccountSigninRequestDto;
import com.youhogeon.icou.dto.ResponseDto;
import com.youhogeon.icou.service.AccountService;

import static com.youhogeon.icou.util.ResponseUtil.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AccountController {

    private final AccountService accountService;
    
    @PostMapping("/signup")
    public ResponseDto<?> createAccount(@Validated @RequestBody AccountCreateRequestDto accountCreateRequestDto) {
        accountService.create(accountCreateRequestDto);

        return ok();
    }

    @PostMapping("/signin")
    public ResponseDto<?> signIn(@Validated @RequestBody AccountSigninRequestDto memberRequestDto) {
        return ok(accountService.signIn(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseDto<?> reissue(@Validated @RequestBody AccountJwtTokenReissueRequestDto requestDto) {
        return ok(accountService.reissue(requestDto));
    }

    @PostMapping("/signout")
    public ResponseDto<?> signOut() {
        accountService.signOut();

        return ok();
    }

}
