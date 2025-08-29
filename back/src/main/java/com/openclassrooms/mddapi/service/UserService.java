package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateRequestDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.UserAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for user-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the id of the user
     * @return the {@link User} entity
     * @throws UserNotFoundException if no user exists with the provided ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }

    @Transactional
    public User updateUser(UserUpdateRequestDto updateRequest, User existingUser) {
        // Check if username is already taken
        if (!existingUser.getUsername().equals(updateRequest.getUsername())) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new UserAlreadyExistsException("User with this username already exists");
            }
        }

        // Check if email is already taken by another user
        if (!existingUser.getEmail().equals(updateRequest.getEmail())) {
            if ( userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new UserAlreadyExistsException("User with this email already exists");
            }
        }

        // Only update username if provided and not blank
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().trim().isEmpty()) {
            existingUser.setUsername(updateRequest.getUsername().trim());
        }

        // Only update email if provided and not blank
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().trim().isEmpty()) {
            existingUser.setEmail(updateRequest.getEmail().trim());
        }

        // Only update password if provided
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        // Save and return
        return userRepository.save(existingUser);
    }
}