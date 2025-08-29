package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.post.PostRequestDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for post-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public List<Post> getSubscribedPosts(User user) {
        List<Subscription> userSubscriptions = subscriptionRepository.findByUserId(user.getId());

        List<Subject> subscribedSubjects = userSubscriptions.stream()
                .map(Subscription::getSubject)
                .toList();

        return postRepository.findBySubjectIn(subscribedSubjects);
    }

    public Post getPostDetails(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public void createPost(PostRequestDto postRequestDto, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));

        // Get the subject
        Subject subject = subjectRepository.findById(postRequestDto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + postRequestDto.getSubjectId()));

        // Build the post
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setAuthor(author);
        post.setSubject(subject);

        // Save
        postRepository.save(post);
    }
}