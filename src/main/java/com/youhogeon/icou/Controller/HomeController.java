package com.youhogeon.icou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youhogeon.icou.repository.AccountRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class HomeController {

    AccountRepository accountRepository;

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }

}
