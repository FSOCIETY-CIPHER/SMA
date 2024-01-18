package com.prophius.sma.repository;

import com.prophius.sma.entities.Likes;
import com.prophius.sma.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
