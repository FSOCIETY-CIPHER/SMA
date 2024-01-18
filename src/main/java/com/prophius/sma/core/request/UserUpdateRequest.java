package com.prophius.sma.core.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    private Long id;

    private String username;

    private String email;

    private String profilePicture;
}
