package com.nhatruong.auth.controller;

import com.nhatruong.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HelloWorldController {

    private final AuthenticationService service;
    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
