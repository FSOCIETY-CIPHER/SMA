package com.prophius.sma.core.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

        @Email(message = "Invalid email")
        @NotBlank(message="field cannot be empty")
        private String email;

        @Length(min=5, max=20, message = "Password length should be between 5 and 20 characters")
        private String password;
}
