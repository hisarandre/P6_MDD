package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.auth.AuthResponseDto;
import com.openclassrooms.mddapi.dto.auth.LoginRequestDto;
import com.openclassrooms.mddapi.dto.auth.RegisterRequestDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

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

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Retrieve the profile information of the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required - missing or invalid JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDto> getCurrentUser(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        log.info("Retrieving current user profile");

        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        log.info("Successfully retrieved profile for user: {}", user.getEmail());

        return ResponseEntity.ok(responseDto);
    }
}