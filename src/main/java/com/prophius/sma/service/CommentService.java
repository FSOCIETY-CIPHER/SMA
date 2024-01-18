package com.prophius.sma.service;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.CommentCreationRequest;
import com.prophius.sma.core.request.CommentUpdateRequest;
import com.prophius.sma.core.response.CommentResponse;
import com.prophius.sma.core.response.PaginationResponse;
import org.springframework.http.ResponseEntity;

public interface CommentService{
    CommentResponse createComment(Long post_id, CommentCreationRequest comment, Long user_id) throws SMAException;

    CommentResponse getComment(Long postId, Long id) throws SMAException;

    PaginationResponse getAllComments(Long postId, int pageNo, int pageSize, String sortBy, String sortDir);

    CommentResponse updateComment(Long postId, Long id, CommentUpdateRequest comment, Long user_id) throws SMAException;

    ResponseEntity<String> deleteComment(Long postId, Long id, Long user_id) throws SMAException;
}
