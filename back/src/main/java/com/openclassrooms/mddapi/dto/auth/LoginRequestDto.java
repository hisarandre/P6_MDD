package com.openclassrooms.mddapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "LoginRequestDto",
        description = "Credentials required to authenticate a user."
)
public class LoginRequestDto {

    @Schema(
            description = "User email address used for login.",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    @NotBlank
    @Size(min = 3, max = 255)
    private String email;

    @Schema(
            description = "User password (minimum 6 characters).",
            example = "P@ssw0rd!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
}
