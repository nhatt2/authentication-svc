package com.nhatruong.auth.controller;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.model.UpdateProfileRequest;
import com.nhatruong.auth.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiConstants.EMPLOYEE_BASE_URL)
@PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('employee:read') or hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserDto>> getProfile() {
        log.info("Method: getProfile() - Employee fetching profile");
        ApiResponse<UserDto> response = employeeService.getProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('employee:read') or hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@RequestBody UpdateProfileRequest request) {
        log.info("Method: updateProfile() - Employee updating profile");
        ApiResponse<UserDto> response = employeeService.updateProfile(request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/operation")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<ApiResponse<String>> employeeOperation() {
        log.info("Method: employeeOperation() - Employee performing employee operation");
        ApiResponse<String> response = employeeService.employeeOperation();
        return ResponseEntity.ok(response);
    }
} 