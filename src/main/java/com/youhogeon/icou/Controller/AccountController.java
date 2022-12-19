package com.youhogeon.icou.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountController {
    
    @GetMapping("")
    public String home() {
        return "<h1>Hello, World</h1>";
    }

}
