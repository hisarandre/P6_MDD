package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDto;
import com.openclassrooms.mddapi.dto.comment.CommentResponseDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(
            @PathVariable @Positive Long postId
    ) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(commentMapper.toCommentResponseDtoList(comments));
    }


    @PostMapping("/post/{postId}")
    public ResponseEntity<Void> addComment(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken,
            @PathVariable @Positive Long postId,
            @RequestBody @Valid @NotNull CommentRequestDto commentRequestDto
    ) {
        User author = authService.getAuthenticatedUser(jwtAuthenticationToken);
        commentService.addComment(postId, commentRequestDto, author);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
