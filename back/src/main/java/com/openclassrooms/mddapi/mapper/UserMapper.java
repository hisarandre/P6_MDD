package com.openclassrooms.mddapi.mapper;


import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity to DTO
    UserResponseDto toUserResponseDto(User user);

}