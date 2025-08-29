package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Subjects", description = "Endpoints for managing subjects and user subscriptions")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final AuthService authService;

    @GetMapping
    @Operation(
            summary = "Get all available subjects",
            description = "Retrieve a list of all available subjects in the system. " +
                    "This endpoint requires authentication to access."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all subjects",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubjectResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during subjects retrieval",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<SubjectResponseDto>> getAllSubjects() {
        log.info("Request to get all subjects");

        List<Subject> subjects = subjectService.findAll();
        List<SubjectResponseDto> response = subjectMapper.toSubjectResponseDtoList(subjects);

        log.info("Successfully retrieved {} subjects", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscriptions/status")
    @Operation(
            summary = "Get all subjects with user subscription status",
            description = "Retrieve all available subjects with subscription status"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subjects with subscription status",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubjectWithSubscriptionResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during subjects retrieval",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<SubjectWithSubscriptionResponseDto>> getAllSubjectsWithSubscriptionStatus(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        List<SubjectWithSubscriptionResponseDto> subjects =
                subjectService.findAllWithSubscriptionStatus(user.getId());
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/subscribed")
    @Operation(
            summary = "Get user's subscribed subjects",
            description = "Retrieve only the subjects that the current authenticated user is subscribed to"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user's subscribed subjects",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubjectResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during subscribed subjects retrieval",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<SubjectResponseDto>> getSubscribedSubjects(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        List<SubjectResponseDto> subjects = subjectService.findSubscribedSubjects(user.getId());
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/{subjectId}/subscribe")
    @Operation(
            summary = "Subscribe to a subject",
            description = "Creates a subscription for the current authenticated user to the specified subject."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully subscribed to the subject",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid subject ID format or subject not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - User is already subscribed to this subject",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during subscription creation",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> subscribeToSubject(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Parameter(
                    description = "The ID of the subject to subscribe to",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer subjectId
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        subjectService.subscribeUserToSubject(user, subjectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subjectId}/unsubscribe")
    @Operation(
            summary = "Unsubscribe from a subject",
            description = "Removes the subscription for the current authenticated user from the specified subject. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully unsubscribed from the subject",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subscription not found - User is not subscribed to this subject",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during subscription removal",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> unsubscribeFromSubject(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Parameter(
                    description = "The ID of the subject to unsubscribe from",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer subjectId
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        subjectService.unsubscribeUserFromSubject(user.getId(), subjectId);
        return ResponseEntity.noContent().build();
    }
}