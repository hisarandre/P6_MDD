package com.openclassrooms.mddapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "RegisterRequestDto",
        description = "Information required to register a new user account."
)
public class RegisterRequestDto {

    @Schema(
            description = "Email address for the new account.",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    @NotBlank
    @Size(min = 3, max = 100)
    private String email;

    @Schema(
            description = "Full name of the new user.",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Schema(
            description = "Password for the new account (minimum 8 characters).",
            example = "P@ssw0rd!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
}
