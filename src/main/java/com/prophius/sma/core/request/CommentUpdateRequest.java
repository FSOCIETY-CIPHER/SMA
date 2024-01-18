package com.prophius.sma.core.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {

    private Long id;

    private String comment;
}
