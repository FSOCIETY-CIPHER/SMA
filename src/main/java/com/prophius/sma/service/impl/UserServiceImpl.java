package com.prophius.sma.service.impl;

import com.prophius.sma.config.JWTService;
import com.prophius.sma.core.enums.Roles;
import com.prophius.sma.core.request.DeleteRequest;
import com.prophius.sma.core.request.LoginRequest;
import com.prophius.sma.core.request.UserCreationRequest;
import com.prophius.sma.core.request.UserUpdateRequest;
import com.prophius.sma.core.response.AuthenticationResponse;
import com.prophius.sma.entities.User;
import com.prophius.sma.core.exception.SMAException;
import com.prophius.sma.repository.UserRepository;
import com.prophius.sma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.remote.JMXAuthenticator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public List<User> getAllUsers (){
        return userRepository.findAll();
    }
    @Override
    public String createUser(UserCreationRequest request) throws SMAException {

        validateNonExistingUser(request.getEmail());

        if (StringUtils.isEmpty(request.getEmail())){
            throw new SMAException("Email can not be empty");
        }
        if (StringUtils.isEmpty(request.getUsername())){
            throw new SMAException("Username can not be empty");
        }
        if (StringUtils.isEmpty(request.getPassword())){
            throw new SMAException("Password can not be empty");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);
        return "User created successfully";
    }

    @Override
    public String updateUser(UserUpdateRequest request) throws SMAException {
        Optional<User> userOpt = userRepository.findById(request.getId());
        if (userOpt.isEmpty()){
           throw new SMAException("User not found");
        }

        User updateUser = userOpt.get();
        updateUser.setUsername(request.getUsername());
        updateUser.setEmail(request.getEmail());
        updateUser.setProfilePicture(request.getProfilePicture());

        userRepository.save(updateUser);

        return "User created successfully";
    }
    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String removeUser(String email) throws SMAException {
        User user = getUser(email)
                .orElseThrow(() -> new SMAException("User does not exist!"));

        userRepository.delete(user);

        return "User successfully removed !";
    }

    @Override
    public String deleteUser(DeleteRequest request) throws SMAException {
        Optional<User> userOpt = userRepository.findById(request.getId());

        if (userOpt.isEmpty()){
            throw new SMAException("User does not exist");
        }

        User existingUser = userOpt.get();

        userRepository.delete(existingUser);
        return "User has been removed successfully";
    }

    @Override
    public String followUser(Long userId, Long followerId) throws SMAException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SMAException("User not found"));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new SMAException("Follower not found"));

        user.getFollowers().add(follower);
        userRepository.save(user);
        return "Followed successfully";
    }

    @Override
    public String unfollowUser(Long userId, Long followerId) throws SMAException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SMAException("User not found"));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new SMAException("Follower not found"));

        user.getFollowers().remove(follower);
        userRepository.save(user);
        return "Unfollowed successfully";
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateUser(LoginRequest request) throws SMAException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user= userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new SMAException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return  new ResponseEntity<>(AuthenticationResponse.builder()
                .token(jwtToken)
                .isAuthenticated(true)
                .message("Log in successful").build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> saveUser(UserCreationRequest request) throws SMAException {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new SMAException("User email already exists");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .profilePicture(request.getProfilePicture())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.USER)
                .build();


        userRepository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return new ResponseEntity<>(AuthenticationResponse.builder().token(jwtToken).message("Account successfully created").build(), HttpStatus.CREATED);
    }

    public void validateNonExistingUser(String email) throws SMAException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new SMAException("User already exists with this email");
        }
    }
}
