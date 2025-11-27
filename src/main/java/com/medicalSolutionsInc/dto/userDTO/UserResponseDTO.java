package com.medicalSolutionsInc.dto.userDTO;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import java.time.LocalDateTime;
public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        boolean active,
        boolean accountBlocked,
        String accessToken,
        String refreshToken,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
