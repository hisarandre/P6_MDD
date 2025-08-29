package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.comment.CommentRequestDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing comments on posts.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * Gets all comments for a specific post.
     *
     * @param postId the ID of the post
     * @return list of comments ordered by creation date
     */
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }

    /**
     * Creates a new comment for a post.
     *
     * @param postId the ID of the post to comment on
     * @param commentRequestDto the comment data
     * @param author the user creating the comment
     * @throws IllegalArgumentException if post not found
     */
    @Transactional
    public void addComment(Long postId, CommentRequestDto commentRequestDto, User author) {
        // Find the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        // Create and save comment
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setAuthor(author);
        comment.setPost(post);

        commentRepository.save(comment);
    }
}