package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.post.PostRequestDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing posts.
 * Handles post retrieval, creation, and subscription-based filtering.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubjectRepository subjectRepository;

    /**
     * Gets all posts from subjects the user is subscribed to.
     *
     * @param user the authenticated user
     * @return list of posts from subscribed subjects
     */
    @Transactional(readOnly = true)
    public List<Post> getSubscribedPosts(User user) {
        List<Subscription> userSubscriptions = subscriptionRepository.findByUserId(user.getId());

        List<Subject> subscribedSubjects = userSubscriptions.stream()
                .map(Subscription::getSubject)
                .toList();

        return postRepository.findBySubjectIn(subscribedSubjects);
    }

    /**
     * Gets detailed information about a specific post.
     *
     * @param postId the ID of the post to retrieve
     * @return the post entity with all details
     * @throws IllegalArgumentException if post not found
     */
    @Transactional(readOnly = true)
    public Post getPostDetails(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    /**
     * Creates a new post in the specified subject.
     *
     * @param postRequestDto the post data (title, content, subject ID)
     * @param author the user creating the post
     * @throws IllegalArgumentException if subject not found
     */
    @Transactional
    public void createPost(PostRequestDto postRequestDto, User author) {
        // Find the subject
        Subject subject = subjectRepository.findById(postRequestDto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + postRequestDto.getSubjectId()));

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setAuthor(author);
        post.setSubject(subject);

        postRepository.save(post);
    }
}