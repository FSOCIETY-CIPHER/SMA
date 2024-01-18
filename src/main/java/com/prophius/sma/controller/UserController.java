package com.prophius.sma.controller;

import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.core.request.UserCreationRequest;
import com.prophius.sma.core.request.DeleteRequest;
import com.prophius.sma.core.request.UserUpdateRequest;
import com.prophius.sma.entities.User;
import com.prophius.sma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/v1/sma")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok().body("Welcome to SMA API");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createUser (UserCreationRequest request) throws SMAException {
        var response = userService.createUser(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Optional<User>> getUser(@PathVariable String email) {
        Optional<User> user = userService.getUser(email);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateUser(UserUpdateRequest request) throws SMAException {
        String response = userService.updateUser(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUser(DeleteRequest request) throws SMAException {
        String response = userService.deleteUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{userId}/follow/{followerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @PathVariable Long followerId) throws SMAException {
        String response = userService.followUser(userId, followerId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{userId}/unfollow/{followerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @PathVariable Long followerId) throws SMAException {
        String response = userService.unfollowUser(userId, followerId);
        return ResponseEntity.ok().body(response);
    }
}
