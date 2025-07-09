package com.nhatruong.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {
    
    @NotBlank
    @Pattern(regexp = "^(ADMIN|EMPLOYEE|NORMAL_USER)$")
    private String role;
} 