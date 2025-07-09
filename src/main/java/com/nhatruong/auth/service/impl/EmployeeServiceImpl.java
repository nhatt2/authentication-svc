package com.nhatruong.auth.service.impl;

import com.nhatruong.auth.dto.User;
import com.nhatruong.auth.dto.UserDto;
import com.nhatruong.auth.dto.response.ApiResponse;
import com.nhatruong.auth.exception.UserNotFoundException;
import com.nhatruong.auth.model.UpdateProfileRequest;
import com.nhatruong.auth.repository.UserRepository;
import com.nhatruong.auth.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;

    @Override
    public ApiResponse<UserDto> getProfile() {
        log.info("Method: getProfile() - Employee fetching profile");
        User currentUser = getCurrentUser();
        UserDto userDto = UserDto.fromEntity(currentUser);
        log.info("Method: getProfile() - Profile retrieved for: {}", currentUser.getEmail());
        return ApiResponse.success("Profile retrieved successfully", userDto);
    }

    @Override
    public ApiResponse<UserDto> updateProfile(String firstName, String lastName) {
        log.info("Method: updateProfile() - Employee updating profile");
        User currentUser = getCurrentUser();
        
        if (firstName != null && !firstName.trim().isEmpty()) {
            currentUser.setFirstName(firstName.trim());
        }
        
        if (lastName != null && !lastName.trim().isEmpty()) {
            currentUser.setLastName(lastName.trim());
        }
        
        var updatedUser = userRepository.save(currentUser);
        UserDto userDto = UserDto.fromEntity(updatedUser);
        log.info("Method: updateProfile() - Profile updated for: {}", currentUser.getEmail());
        return ApiResponse.success("Profile updated successfully", userDto);
    }

    @Override
    public ApiResponse<String> employeeOperation() {
        log.info("Method: employeeOperation() - Employee performing employee operation");
        User currentUser = getCurrentUser();
        log.info("Method: employeeOperation() - Operation performed by: {}", currentUser.getEmail());
        return ApiResponse.success("Employee operation completed successfully", "Employee operation result");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
    }
} 