package com.prophius.sma.service.impl;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.PostCreationRequest;
import com.prophius.sma.core.request.PostUpdateRequest;
import com.prophius.sma.core.response.PaginationResponse;
import com.prophius.sma.core.response.PostResponse;
import com.prophius.sma.entities.Post;
import com.prophius.sma.entities.User;
import com.prophius.sma.repository.PostRepository;
import com.prophius.sma.repository.UserRepository;
import com.prophius.sma.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponse createPost(PostCreationRequest request, Long user_id) throws SMAException {

        if (StringUtils.isEmpty(request.getContent())){
            throw new SMAException("Content can not be empty");
        }

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new SMAException("User not found"));

        Post newPost = Post.builder()
                .content(request.getContent())
                .creationDate(LocalDateTime.now())
                .createdBy(user)
                .build();

        return mapToResponse(postRepository.save(newPost));
    }

    @Override
    public PostResponse getPostById(Long id) throws SMAException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SMAException("Post not found"));

        return mapToResponse(post);    }

    @Override
    public PaginationResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> postsPerPage = posts.getContent();
        List<PostResponse> pageContent =  postsPerPage.stream().map(post -> mapToResponse(post)).collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .isLastPage(posts.isLast())
                .pageContent(pageContent)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages()).build();
        return paginationResponse;
    }

    @Override
    public PostResponse updatePost(PostUpdateRequest post, Long id, Long user_id) throws SMAException {
        Post postDB = postRepository.findById(id)
                .orElseThrow(() -> new SMAException("Post not found"));

        if(!Objects.equals(postDB.getCreatedBy().getId(), user_id)){
            throw new SMAException("You cannot edit this post");
        }

        postDB.setContent(post.getContent());
        Post updated = postRepository.save(postDB);

        return mapToResponse(updated);
    }

    @Override
    public ResponseEntity<String> deletePost(Long id, Long user_id) throws SMAException {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SMAException("Post not found"));

        if(!Objects.equals(post.getCreatedBy().getId(), user_id)){
            throw new SMAException("You cannot delete this post");
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok("Post deleted");
    }

    @Override
    public PaginationResponse getAllPostsByUserId(int pageNo, int pageSize, String sortBy, String sortDir, Long user_id) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findByCreatedBy(user_id, pageable);
        //get contents per page
        List<Post> postsPerPage = posts.getContent();
        List<PostResponse> pageContent =  postsPerPage.stream()
                .map(post -> mapToResponse(post)).collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .isLastPage(posts.isLast())
                .pageContent(pageContent)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages()).build();
        return paginationResponse;

    }

    private PostResponse mapToResponse(Post post){
        return PostResponse.builder()
                .id(post.getId())
                .comments(post.getComments().stream().map(comment-> CommentServiceImpl.mapToCommentResponse(comment)).collect(Collectors.toSet()))
                .likesList((long) post.getLikesList().size())
                .content(post.getContent()).build();
    }

}
