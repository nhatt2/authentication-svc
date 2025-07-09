package com.nhatruong.auth.service.impl;

import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.constant.security.UserRole;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.exception.UserNotFoundException;
import com.nhatruong.auth.repository.UserRepository;
import com.nhatruong.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {
        log.info("Method: getAllUsers() - Admin fetching all users");
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
        log.info("Method: getAllUsers() - Found {} users", users.size());
        return ApiResponse.success("Users retrieved successfully", users);
    }

    @Override
    public ApiResponse<UserDto> getUserById(Integer id) {
        log.info("Method: getUserById() - Admin fetching user with ID: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        UserDto userDto = UserDto.fromEntity(user);
        log.info("Method: getUserById() - User found: {}", userDto.getEmail());
        return ApiResponse.success("User retrieved successfully", userDto);
    }

    @Override
    public ApiResponse<String> deleteUser(Integer id) {
        log.info("Method: deleteUser() - Admin deleting user with ID: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
        log.info("Method: deleteUser() - User deleted successfully: {}", user.getEmail());
        return ApiResponse.success("User deleted successfully");
    }

    @Override
    public ApiResponse<UserDto> updateUserRole(Integer id, String role) {
        log.info("Method: updateUserRole() - Admin updating user role for ID: {} to role: {}", id, role);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            user.setUserRole(userRole);
            var updatedUser = userRepository.save(user);
            UserDto userDto = UserDto.fromEntity(updatedUser);
            log.info("Method: updateUserRole() - User role updated successfully for: {}", user.getEmail());
            return ApiResponse.success("User role updated successfully", userDto);
        } catch (IllegalArgumentException e) {
            log.error("Method: updateUserRole() - Invalid role: {}", role);
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public ApiResponse<String> adminOperation() {
        log.info("Method: adminOperation() - Admin performing admin operation");
        return ApiResponse.success("Admin operation completed successfully", "Admin operation result");
    }
} 