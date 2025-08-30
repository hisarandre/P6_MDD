package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySubjectIn(List<Subject> subjects);
    List<Post> findBySubjectInOrderByCreatedAtAsc(List<Subject> subjects);
    List<Post> findBySubjectInOrderByCreatedAtDesc(List<Subject> subjects);

}