package com.prophius.sma.service.impl;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.CommentCreationRequest;
import com.prophius.sma.core.request.CommentUpdateRequest;
import com.prophius.sma.core.response.CommentResponse;
import com.prophius.sma.core.response.PaginationResponse;
import com.prophius.sma.entities.Comment;
import com.prophius.sma.entities.Post;
import com.prophius.sma.entities.User;
import com.prophius.sma.repository.CommentRepository;
import com.prophius.sma.repository.PostRepository;
import com.prophius.sma.repository.UserRepository;
import com.prophius.sma.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponse createComment(Long post_id, CommentCreationRequest comment, Long user_id) throws SMAException {
        User user = userRepository.findById(user_id)
                .orElseThrow(() ->  new SMAException("User already exists with this email"));


        Comment commentDB = new Comment();
        Post post = postRepository.findById(post_id)
                .orElseThrow(()->new SMAException("Post not found"));

        post.addComment(commentDB);
        commentDB.setPost(post);
        commentDB.setUser(user);

        return mapToCommentResponse(commentRepository.save(commentDB), user);
    }

    @Override
    public CommentResponse getComment(Long postId, Long id) throws SMAException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->new SMAException("Comment not found"));
        return mapToCommentResponse(comment);
    }

    @Override
    public PaginationResponse getAllComments(Long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);

        List<Comment> commentsPerPage = comments.getContent();
        List<CommentResponse> pageContent = commentsPerPage.stream()
                .map(comment -> mapToCommentResponse(comment))
                .collect(Collectors.toList());

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .pageSize(pageSize)
                .pageNo(pageNo)
                .isLastPage(comments.isLast())
                .pageContent(pageContent)
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();

        return paginationResponse;
    }

    @Override
    public CommentResponse updateComment(Long postId, Long id, CommentUpdateRequest comment, Long user_id) throws SMAException {
        Comment commentDB = validatePostComment(postId, id, user_id);
        commentDB.setComment(comment.getComment());

        var userCheck = userRepository.findById(user_id);
        User user = new User();

        if (userCheck.isPresent()){
            user = userCheck.get();
        }
        return mapToCommentResponse(commentRepository.save(commentDB), user);
    }

    @Override
    public ResponseEntity<String> deleteComment(Long postId, Long id, Long user_id) throws SMAException {
        Comment comment = validatePostComment(postId, id, user_id);
        commentRepository.deleteById(comment.getId());
        return ResponseEntity.ok("Comment deleted");
    }

    public static CommentResponse mapToCommentResponse(Comment comment, User user) {
        return CommentResponse.builder()
                .comment(comment.getComment())
                .email(user.getEmail())
                .id(comment.getId()).build();
    }

    public static CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .comment(comment.getComment())
                .email(comment.getUser().getEmail())
                .id(comment.getId()).build();
    }

    private Comment validatePostComment(Long post_Id, Long id, Long user_id) throws SMAException {
        Post post;
        post = postRepository.findById(post_Id)
                .orElseThrow(()->new SMAException("Post not found"));
        Comment commentDB = commentRepository.findById(id)
                .orElseThrow(()-> new SMAException("Comment not found"));

        if(!commentDB.getPost().getId().equals(post.getId())){
            throw new SMAException("Comment does not belong to this post");
        }
        if(user_id != commentDB.getUser().getId()){
            throw new SMAException("Comment does not belong to this user");
        }
        return commentDB;
    }
}
