package com.openclassrooms.mddapi.controller;



import com.openclassrooms.mddapi.dto.post.PostDetailsResponseDto;
import com.openclassrooms.mddapi.dto.post.PostRequestDto;
import com.openclassrooms.mddapi.dto.post.PostResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints for retrieving posts")
public class PostController {

    private final PostService postService;
    private final AuthService authService;
    private final PostMapper postMapper;

    @GetMapping("/subscribed")
    public ResponseEntity<List<PostResponseDto>> getSubscribedPosts(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        List<Post> posts = postService.getSubscribedPosts(user);
        return ResponseEntity.ok(postMapper.toPostResponseDtoList(posts));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailsResponseDto> getPostDetails(@PathVariable @Positive Long postId) {
        Post post = postService.getPostDetails(postId);
        return ResponseEntity.ok(postMapper.toPostDetailsResponseDto(post));
    }

    @PostMapping()
    public ResponseEntity<Void> createPost(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @RequestBody @Valid @NotNull PostRequestDto postRequestDto
    ) {
        User author = authService.getAuthenticatedUser(jwtAuthenticationToken);

        postService.createPost(postRequestDto, author);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}