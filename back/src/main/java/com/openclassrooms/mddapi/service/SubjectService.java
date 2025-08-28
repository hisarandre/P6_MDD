package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for subject-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubjectMapper subjectMapper;


    /**
     * Retrieve all subjects
     * @return List of Subject
     */
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public List<SubjectWithSubscriptionResponseDto> findAllWithSubscriptionStatus(Long userId) {
        List<Subject> allSubjects = subjectRepository.findAll();
        List<Long> subscribedIds = subscriptionRepository.findByUserId(userId).stream()
                .map(sub -> sub.getSubject().getId())
                .toList();

        return subjectMapper.toSubjectWithSubscriptionDtoList(allSubjects, subscribedIds);
    }

    public List<SubjectResponseDto> findSubscribedSubjects(Long userId) {
        List<Subscription> userSubscriptions = subscriptionRepository.findByUserId(userId);

        List<Subject> subscribedSubjects = userSubscriptions.stream()
                .map(Subscription::getSubject)
                .toList();

        return subjectMapper.toSubjectResponseDtoList(subscribedSubjects);
    }

    public void subscribeUserToSubject(User user, Integer subjectId) {
        // Check if subscription already exists
        boolean exists = subscriptionRepository.existsByUserIdAndSubjectId(user.getId(), subjectId.longValue());
        if (exists) {
            throw new IllegalStateException("User is already subscribed to this subject");
        }

        // Fetch subject
        Subject subject = subjectRepository.findById(subjectId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        // Create and save subscription
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubject(subject);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeUserFromSubject(Long userId, Integer subjectId) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndSubjectId(userId, subjectId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }

}