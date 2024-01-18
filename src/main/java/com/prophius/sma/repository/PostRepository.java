package com.prophius.sma.repository;

import com.prophius.sma.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCreatedBy(Long userId, Pageable pageable);

}
