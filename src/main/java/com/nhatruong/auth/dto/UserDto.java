package com.nhatruong.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhatruong.auth.constant.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }
} 