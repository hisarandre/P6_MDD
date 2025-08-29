package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateRequestDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.SubjectService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for retrieving user information")
public class UserController {

    private final UserService userService;
    private final SubjectService subjectService;
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
                    description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "The id of the user", required = true)
            @PathVariable Long id
    ) {
        User user = userService.getUserById(id);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Retrieve the profile information of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User profile retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Token is expired or lacks required scope",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponseDto> me(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }


    @PutMapping("/me/update")
    @Operation(
            summary = "Update current user profile",
            description = "Update the profile information of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User profile updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Token is expired or lacks required scope",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflict - Username or email already exists",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Valid @RequestBody @NotNull UserUpdateRequestDto userUpdateRequestDto
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);

        User updatedUser = userService.updateUser(userUpdateRequestDto, user);

        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }

}