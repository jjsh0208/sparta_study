package com.sparta.rediscachepractice2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String loginForm(){
        return "login-Form";
    }


    @GetMapping("my-profile")
    public String myProfile(){
        return "my-profile";
    }

}
