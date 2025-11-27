package com.medicalSolutionsInc.controller.user;
import com.medicalSolutionsInc.config.ApiResponseConfig.ApiResponseConfig;
import com.medicalSolutionsInc.dto.userDTO.AuthRequestDTO;
import com.medicalSolutionsInc.dto.userDTO.UserResponseDTO;
import com.medicalSolutionsInc.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication & User Management Endpoints", description = "APIs for authentication and user management")
@RequestMapping("/api/${API_VERSION}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    public ResponseEntity<ApiResponseConfig<UserResponseDTO>> register(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        UserResponseDTO userResponseDTO = userService.registerUser(authRequestDTO);
        ApiResponseConfig<UserResponseDTO> response = new ApiResponseConfig<>(
                "User registered successfully. Please check your email for the 2FA code.",
                userResponseDTO
        );
        return ResponseEntity.ok(response);
    }
}
