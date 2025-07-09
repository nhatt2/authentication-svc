package com.nhatruong.auth.controller;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiConstants.DEMO_BASE_URL)
public class DemoController {

    @GetMapping
    public ResponseEntity<ApiResponse<String>> sayHello() {
        log.info("Method: sayHello() - Demo endpoint accessed");
        return ResponseEntity.ok(ApiResponse.success("Hello from secured endpoint"));
    }
} 