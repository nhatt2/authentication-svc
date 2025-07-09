package com.nhatruong.auth.service;

import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.model.UpdateProfileRequest;

public interface EmployeeService {
    ApiResponse<UserDto> getProfile();
    ApiResponse<UserDto> updateProfile(String firstName, String lastName);
    ApiResponse<String> employeeOperation();
} 