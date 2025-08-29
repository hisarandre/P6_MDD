package com.openclassrooms.mddapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsResponseDto {
    Long id;
    String title;
    String content;
    String author;
    String subject;
    LocalDateTime createdAt;
}