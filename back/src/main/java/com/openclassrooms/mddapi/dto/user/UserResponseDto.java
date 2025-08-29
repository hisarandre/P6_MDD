package com.openclassrooms.mddapi.dto.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    Integer id;
    String username;
    String email;

    @JsonFormat(pattern = "yyyy/MM/dd")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy/MM/dd")
    LocalDateTime updatedAt;
}