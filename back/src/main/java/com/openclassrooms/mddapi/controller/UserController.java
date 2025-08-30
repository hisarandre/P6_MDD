package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateRequestDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing user profiles and information")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user's profile information by their ID."
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
                    responseCode = "400",
                    description = "Invalid user ID provided",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the given ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(
                    description = "The unique ID of the user to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        log.info("Retrieving user profile for ID: {}", id);

        User user = userService.getUserById(id);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        log.info("Successfully retrieved user profile for ID: {}", id);

        return ResponseEntity.ok(responseDto);
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


    @PutMapping("/me")
    @Operation(
            summary = "Update current user profile",
            description = "Update the profile information of the currently authenticated user. " +
                    "Only provided fields will be updated.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation errors",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required - missing or invalid JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - username or email already exists",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed for update data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDto> updateCurrentUser(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Parameter(
                    description = "Updated user information (only provided fields will be updated)",
                    required = true
            )
            @Valid @RequestBody @NotNull(message = "Update request cannot be null") UserUpdateRequestDto userUpdateRequestDto
    ) {
        log.info("Updating current user profile");

        User currentUser = authService.getAuthenticatedUser(jwtAuthenticationToken);
        User updatedUser = userService.updateUser(userUpdateRequestDto, currentUser);
        UserResponseDto responseDto = userMapper.toUserResponseDto(updatedUser);

        log.info("Successfully updated profile for user: {}", updatedUser.getEmail());

        return ResponseEntity.ok(responseDto);
    }
}