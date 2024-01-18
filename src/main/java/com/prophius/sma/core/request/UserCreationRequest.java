package com.prophius.sma.core.request;

import com.prophius.sma.core.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {

    private String username;

    private String email;

    private String password;

    private String profilePicture;

    private Roles role;
}
