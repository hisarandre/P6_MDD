package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.auth.AuthResponseDto;
import com.openclassrooms.mddapi.dto.auth.LoginRequestDto;
import com.openclassrooms.mddapi.dto.auth.RegisterRequestDto;
import com.openclassrooms.mddapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user account",
            description = "Create a new user account with email, username, and password. " +
                    "Returns a JWT token for immediate authentication.",
            security = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A user with the provided email or username already exists",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during registration",
                    content = @Content
            )
    })
    public ResponseEntity<AuthResponseDto> registerUser(
            @Parameter(
                    description = "User registration details including email, username, and password",
                    required = true
            )
            @Valid @RequestBody RegisterRequestDto registerRequestDto
    ) {
        log.info("Registration attempt for email: {}", registerRequestDto.getEmail());

        String token = authService.registerUser(registerRequestDto);

        log.info("User registered successfully with email: {}", registerRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDto(token));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user login",
            description = "Authenticate a user with email and password to obtain a JWT token. " +
                    "The token can be used for accessing protected endpoints.",
            security = {}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials - email or password is incorrect",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during authentication",
                    content = @Content
            )
    })
    public ResponseEntity<AuthResponseDto> loginUser(
            @Parameter(
                    description = "User login credentials including email and password",
                    required = true
            )
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        log.info("Login attempt for email: {}", loginRequestDto.getEmail());

        String token = authService.loginUser(loginRequestDto);

        log.info("User logged in successfully with email: {}", loginRequestDto.getEmail());

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}