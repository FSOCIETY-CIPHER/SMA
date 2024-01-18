package com.prophius.sma;

import com.prophius.sma.config.JWTService;
import com.prophius.sma.controller.AuthController;
import com.prophius.sma.controller.UserController;
import com.prophius.sma.core.enums.Roles;
import com.prophius.sma.core.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prophius.sma.core.request.UserCreationRequest;
import com.prophius.sma.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    public UserControllerTest(UserService userService, JWTService jwtService, ObjectMapper objectMapper, AuthController authController) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.authController = authController;
    }


    @Test
    public void testRegisterUser() throws Exception {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("testUser");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setProfilePicture("profilePicUrl");
        request.setRole(Roles.USER);

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .message("Account successfully created")
                .token("sampleToken")
                .build();

        given(userService.saveUser(any(UserCreationRequest.class)))
                .willReturn(new ResponseEntity<>(expectedResponse, HttpStatus.CREATED));

        mockMvc.perform(post("/api/v1/sma/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }





    @Test
    public void testCreateUser() throws Exception {
        UserCreationRequest request = new UserCreationRequest(); // set request parameters
        String expectedResponse = "User created successfully";

        given(userService.createUser(any(UserCreationRequest.class))).willReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/sma/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testFollowUser() throws Exception {
        Long userId = 1L, followerId = 2L;
        String expectedResponse = "Followed successfully";

        given(userService.followUser(userId, followerId)).willReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/sma/user/" + userId + "/follow/" + followerId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testUnfollowUser() throws Exception {
        Long userId = 1L, followerId = 2L;
        String expectedResponse = "Unfollowed successfully";

        given(userService.unfollowUser(userId, followerId)).willReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/sma/user/" + userId + "/unfollow/" + followerId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    // Test methods will go here
}
