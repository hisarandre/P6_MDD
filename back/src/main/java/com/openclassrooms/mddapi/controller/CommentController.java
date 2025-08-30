package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDto;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Endpoints for managing post comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    @GetMapping("/post/{postId}")
    @Operation(
            summary = "Get comments for a post",
            description = "Retrieve all comments for a specific post, ordered by creation date (newest first). " +
                    "This endpoint is publicly accessible."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid post ID provided",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found with the given ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while retrieving comments",
                    content = @Content
            )
    })
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(
            @Parameter(
                    description = "ID of the post to retrieve comments for",
                    required = true,
                    example = "1"
            )
            @PathVariable Long postId
    ) {
        log.info("Retrieving comments for post with ID: {}", postId);

        List<Comment> comments = commentService.getCommentsByPostId(postId);
        List<CommentResponseDto> commentDtos = commentMapper.toCommentResponseDtoList(comments);

        log.info("Successfully retrieved {} comments for post ID: {}", commentDtos.size(), postId);

        return ResponseEntity.ok(commentDtos);
    }

    @PostMapping("/post/{postId}")
    @Operation(
            summary = "Add a comment to a post",
            description = "Create a new comment for a post. Requires authentication. " +
                    "The comment will be associated with the authenticated user as the author.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment added successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or post ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required - invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found with the given ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed for comment content",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while creating comment",
                    content = @Content
            )
    })
    public ResponseEntity<Void> addComment(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Parameter(
                    description = "ID of the post to add the comment to",
                    required = true,
                    example = "1"
            )
            @PathVariable @Positive(message = "Post ID must be positive") Long postId,
            @Parameter(
                    description = "Comment data including content",
                    required = true
            )
            @RequestBody @Valid @NotNull(message = "Comment request cannot be null") CommentRequestDto commentRequestDto
    ) {
        log.info("Adding comment to post with ID: {} by user", postId);

        User author = authService.getAuthenticatedUser(jwtAuthenticationToken);
        commentService.addComment(postId, commentRequestDto, author);

        log.info("Successfully added comment to post ID: {} by user: {}", postId, author.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}