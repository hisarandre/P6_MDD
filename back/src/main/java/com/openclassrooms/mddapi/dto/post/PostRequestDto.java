package com.openclassrooms.mddapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "The title is required")
    @Size(min = 3, max = 255, message = "The title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "The content is required")
    @Size(min = 10, message = "The content must be more than 10 characters")
    private String content;

    @NotNull(message = "The subjectId is required")
    private Long subjectId;
}
