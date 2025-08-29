package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for user-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public void addComment(Long postId, CommentRequestDto commentRequestDto, User author) {
        // Get the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        // Build the post
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setAuthor(author);
        comment.setPost(post);

        // Save
        commentRepository.save(comment);
    }
}