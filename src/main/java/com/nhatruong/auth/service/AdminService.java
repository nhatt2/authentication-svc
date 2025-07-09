package com.nhatruong.auth.service;

import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.dto.response.ApiResponse;

import java.util.List;

public interface AdminService {
    ApiResponse<List<UserDto>> getAllUsers();
    ApiResponse<UserDto> getUserById(Integer id);
    ApiResponse<String> deleteUser(Integer id);
    ApiResponse<UserDto> updateUserRole(Integer id, String role);
    ApiResponse<String> adminOperation();
} 