package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.post.PostDetailsResponseDto;
import com.openclassrooms.mddapi.dto.post.PostRequestDto;
import com.openclassrooms.mddapi.dto.post.PostResponseDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints for managing posts")
public class PostController {

    private final PostService postService;
    private final AuthService authService;
    private final PostMapper postMapper;

    @GetMapping("/subscribed")
    @Operation(
            summary = "Get subscribed posts",
            description = "Retrieve posts from subjects the authenticated user is subscribed to",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Posts retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<List<PostResponseDto>> getSubscribedPosts(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        log.info("Retrieving subscribed posts for authenticated user");

        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        List<Post> posts = postService.getSubscribedPosts(user);
        List<PostResponseDto> postDtos = postMapper.toPostResponseDtoList(posts);

        log.info("Retrieved {} subscribed posts for user: {}", postDtos.size(), user.getEmail());

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/{postId}")
    @Operation(
            summary = "Get post details",
            description = "Retrieve detailed information about a specific post including comments"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post details retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDetailsResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid post ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<PostDetailsResponseDto> getPostDetails(
            @Parameter(
                    description = "ID of the post to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable @Positive(message = "Post ID must be positive") Long postId
    ) {
        log.info("Retrieving details for post ID: {}", postId);

        Post post = postService.getPostDetails(postId);
        PostDetailsResponseDto postDetails = postMapper.toPostDetailsResponseDto(post);

        log.info("Successfully retrieved post details for ID: {}", postId);

        return ResponseEntity.ok(postDetails);
    }

    @PostMapping
    @Operation(
            summary = "Create a new post",
            description = "Create a new post in a specific subject. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post created successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subject not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<Void> createPost(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @Parameter(
                    description = "Post data including title, content, and subject ID",
                    required = true
            )
            @RequestBody @Valid @NotNull(message = "Post request cannot be null") PostRequestDto postRequestDto
    ) {
        log.info("Creating new post in subject ID: {}", postRequestDto.getSubjectId());

        User author = authService.getAuthenticatedUser(jwtAuthenticationToken);
        postService.createPost(postRequestDto, author);

        log.info("Successfully created post by user: {} in subject ID: {}",
                author.getEmail(), postRequestDto.getSubjectId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}