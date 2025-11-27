package com.medicalSolutionsInc.mappers.userMapper;
import com.medicalSolutionsInc.dto.userDTO.AuthRequestDTO;
import com.medicalSolutionsInc.dto.userDTO.UserResponseDTO;
import com.medicalSolutionsInc.entity.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Create a new User from registration/auth request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "accountBlocked", constant = "false")
    User toEntity(AuthRequestDTO dto);

    // Convert User entity to response DTO
    UserResponseDTO toResponseDTO(User user);

    // Optional: update existing User from AuthRequestDTO (e.g. admin edits user)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(AuthRequestDTO dto, @org.mapstruct.MappingTarget User user);
}

