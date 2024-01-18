package com.prophius.sma.core.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;

    private String content;

    private Long likesList;

    private Set<CommentResponse> comments;
}
