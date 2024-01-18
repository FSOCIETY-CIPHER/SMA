package com.prophius.sma.controller;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.PostCreationRequest;
import com.prophius.sma.core.request.PostUpdateRequest;
import com.prophius.sma.core.response.PaginationResponse;
import com.prophius.sma.core.response.PostResponse;
import com.prophius.sma.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/sma/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/{user_id}")
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreationRequest post,
            @PathVariable Long user_id
    ) throws SMAException {
        return new ResponseEntity<>(postService.createPost(post, user_id), HttpStatus.CREATED);
    }

    @GetMapping()
    public PaginationResponse getAllPosts(
            @RequestParam(value="pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{user_id}")
    public PaginationResponse getAllPostsById(
            @RequestParam(value="pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false) String sortDir,
            @PathVariable Long user_id
    ){
        return postService.getAllPostsByUserId(pageNo, pageSize, sortBy, sortDir, user_id);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long id
    ) throws SMAException {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.FOUND);
    }

    @PutMapping("/{id}/{user_id}")
    public ResponseEntity<PostResponse> editPost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest post,
            @PathVariable Long user_id
    ) throws SMAException {
        return ResponseEntity.ok(postService.updatePost(post, id, user_id));
    }

    @DeleteMapping("/{id}/{user_id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            @PathVariable Long user_id
    ) throws SMAException {
        return postService.deletePost(id, user_id);
    }
}
