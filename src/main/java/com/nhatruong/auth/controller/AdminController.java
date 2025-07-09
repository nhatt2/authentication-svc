package com.nhatruong.auth.controller;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.model.UpdateRoleRequest;
import com.nhatruong.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiConstants.ADMIN_BASE_URL)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        log.info("Method: getAllUsers() - Admin fetching all users");
        ApiResponse<List<UserDto>> response = adminService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
        log.info("Method: getUserById() - Admin fetching user with ID: {}", id);
        ApiResponse<UserDto> response = adminService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Integer id) {
        log.info("Method: deleteUser() - Admin deleting user with ID: {}", id);
        ApiResponse<String> response = adminService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<UserDto>> updateUserRole(
            @PathVariable Integer id,
            @RequestBody UpdateRoleRequest request) {
        log.info("Method: updateUserRole() - Admin updating user role for ID: {} to role: {}", id, request.getRole());
        ApiResponse<UserDto> response = adminService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/operation")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<String>> adminOperation() {
        log.info("Method: adminOperation() - Admin performing admin operation");
        ApiResponse<String> response = adminService.adminOperation();
        return ResponseEntity.ok(response);
    }
} 