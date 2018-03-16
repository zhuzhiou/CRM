package com.crm.modules.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginController {

    @GetMapping
    public String doGet() {
        return "security/login";
    }

    @PostMapping
    public String doPost() {
        return "security/login";
    }
}
