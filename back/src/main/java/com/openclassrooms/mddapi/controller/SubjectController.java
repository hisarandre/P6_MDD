package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.PostService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Subjects", description = "Endpoints for retrieving subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieve all available subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subjects",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<SubjectResponseDto>> getAllSubjects() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(subjectMapper.toSubjectResponseDtoList(subjects));
    }


    @GetMapping("/subscriptions/status")
    @Operation(
            summary = "Get all subjects with authenticated user's subscription status",
            description = "Retrieve all subjects with indication of which ones the current user is subscribed to"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subjects with subscription status",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectWithSubscriptionResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
            summary = "Get all subjects the current user is subscribed to",
            description = "Retrieve only the subjects that the current user is subscribed to"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscribed subjects",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
            summary = "Subscribe authenticated user to a subject",
            description = "Creates a subscription for the current user to the specified subject."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid subject ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "User is already subscribed to this subject")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> subscribeToSubject(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable Integer subjectId
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        subjectService.subscribeUserToSubject(user, subjectId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{subjectId}/unsubscribe")
    @Operation(
            summary = "Unsubscribe authenticated user from a subject",
            description = "Removes the subscription for the current user from the specified subject."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> unsubscribeFromSubject(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable Integer subjectId
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        subjectService.unsubscribeUserFromSubject(user.getId(), subjectId);
        return ResponseEntity.noContent().build();
    }
}