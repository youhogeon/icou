package com.youhogeon.icou.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.youhogeon.icou.controller.dto.request.AccountCreateRequestDto;
import com.youhogeon.icou.controller.dto.request.AccountJwtTokenReissueRequestDto;
import com.youhogeon.icou.controller.dto.request.AccountSigninRequestDto;
import com.youhogeon.icou.controller.dto.response.JwtTokenResponseDto;
import com.youhogeon.icou.controller.dto.response.ResponseDto;
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
    public ResponseDto<?> signIn(@Validated @RequestBody AccountSigninRequestDto accountSigninRequestDto) {
        JwtTokenResponseDto jwtTokenResponseDto = accountService.signIn(accountSigninRequestDto);

        return ok(jwtTokenResponseDto);
    }

    @PostMapping("/reissue")
    public ResponseDto<?> reissue(@Validated @RequestBody AccountJwtTokenReissueRequestDto requestDto) {
        JwtTokenResponseDto jwtTokenResponseDto = accountService.reissue(requestDto);

        return ok(jwtTokenResponseDto);
    }

    @PostMapping("/signout")
    public ResponseDto<?> signOut() {
        accountService.signOut();

        return ok();
    }

}
