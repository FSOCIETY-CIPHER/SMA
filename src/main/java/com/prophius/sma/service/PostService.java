package com.prophius.sma.service;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.PostCreationRequest;
import com.prophius.sma.core.request.PostUpdateRequest;
import com.prophius.sma.core.response.PaginationResponse;
import com.prophius.sma.core.response.PostResponse;
import com.prophius.sma.entities.Post;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface PostService {
    PostResponse createPost(PostCreationRequest request, Long user_id) throws SMAException;

    PostResponse getPostById(Long id) throws SMAException;

    PaginationResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostResponse updatePost(PostUpdateRequest post, Long id, Long user_id) throws SMAException;

    ResponseEntity<String> deletePost(Long id, Long user_id) throws SMAException;

    PaginationResponse getAllPostsByUserId(int pageNo, int pageSize, String sortBy, String sortDir, Long userId);
}
