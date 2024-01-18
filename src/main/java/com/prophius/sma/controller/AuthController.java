package com.prophius.sma.controller;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.LoginRequest;
import com.prophius.sma.core.request.UserCreationRequest;
import com.prophius.sma.core.response.AuthenticationResponse;
import com.prophius.sma.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/sma/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserCreationRequest request) throws SMAException {
        return userService.saveUser(request);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> logUserIn(@Valid @RequestBody LoginRequest request) throws SMAException {
        return userService.authenticateUser(request);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok().body("Welcome to SMA API");
    }
}
