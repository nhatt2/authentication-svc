package com.nhatruong.auth.model;

import com.nhatruong.auth.constant.security.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    private String username;
    
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstname;
    
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastname;
    
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;
    
    @NotBlank
    @Size(min = 8, max = 100)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")
    private String password;
    
    @NotNull
    private UserRole role;
}
