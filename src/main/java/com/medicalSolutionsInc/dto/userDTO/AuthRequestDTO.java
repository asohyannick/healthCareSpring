package com.medicalSolutionsInc.dto.userDTO;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import jakarta.validation.constraints.*;
public record AuthRequestDTO(

        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @NotBlank(message = "First name is required")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character"
        )
        String password,

        @NotNull(message = "User role is required")
        UserRole role
) { }
