package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.Subscription;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * Retrieves all available subjects.
     *
     * @return List of all {@link Subject} entities
     */
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    /**
     * Retrieves all subjects with subscription status for a specific user.
     *
     * @param userId the ID of the user to check subscription status for
     * @return List of {@link SubjectWithSubscriptionResponseDto} with subscription status
     */
    public List<SubjectWithSubscriptionResponseDto> findAllWithSubscriptionStatus(Long userId) {
        List<Subject> allSubjects = subjectRepository.findAll();
        List<Long> subscribedIds = subscriptionRepository.findByUserId(userId).stream()
                .map(sub -> sub.getSubject().getId())
                .toList();

        return subjectMapper.toSubjectWithSubscriptionDtoList(allSubjects, subscribedIds);
    }

    /**
     * Retrieves all subjects that a specific user is subscribed to.
     *
     * @param userId the ID of the user
     * @return List of {@link SubjectResponseDto} representing subscribed subjects
     */
    public List<SubjectResponseDto> findSubscribedSubjects(Long userId) {
        List<Subscription> userSubscriptions = subscriptionRepository.findByUserId(userId);
        List<Subject> subscribedSubjects = userSubscriptions.stream()
                .map(Subscription::getSubject)
                .toList();

        return subjectMapper.toSubjectResponseDtoList(subscribedSubjects);
    }

    /**
     * Creates a subscription between a user and a subject.
     *
     * @param user the {@link User} entity to subscribe
     * @param subjectId the ID of the subject to subscribe to
     * @throws IllegalStateException if the user is already subscribed to the subject
     * @throws IllegalArgumentException if the subject is not found
     */
    @Transactional
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

    /**
     * Removes a subscription between a user and a subject.
     *
     * @param userId the ID of the user to unsubscribe
     * @param subjectId the ID of the subject to unsubscribe from
     * @throws IllegalArgumentException if the subscription is not found
     */
    @Transactional
    public void unsubscribeUserFromSubject(Long userId, Integer subjectId) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndSubjectId(userId, subjectId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }
}