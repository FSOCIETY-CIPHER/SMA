package com.prophius.sma.service;

import com.prophius.sma.core.request.DeleteRequest;
import com.prophius.sma.core.request.LoginRequest;
import com.prophius.sma.core.request.UserCreationRequest;
import com.prophius.sma.core.request.UserUpdateRequest;
import com.prophius.sma.core.response.AuthenticationResponse;
import com.prophius.sma.entities.User;
import com.prophius.sma.core.exception.SMAException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    String createUser(UserCreationRequest request) throws SMAException;

    String updateUser(UserUpdateRequest request) throws SMAException;

    Optional<User> getUser(String userEmail);

    String removeUser(String email) throws SMAException;

    String deleteUser(DeleteRequest request) throws SMAException;

    String followUser(Long userId, Long followerId) throws SMAException;

    String unfollowUser(Long userId, Long followerId) throws SMAException;

    public ResponseEntity<AuthenticationResponse> authenticateUser(LoginRequest request) throws SMAException;

    ResponseEntity<AuthenticationResponse> saveUser(UserCreationRequest request) throws SMAException;
}
