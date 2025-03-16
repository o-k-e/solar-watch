package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.MemberResponse;
import com.codecool.solarwatch.repository.MemberRepository;
import com.codecool.solarwatch.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class) //It enables Spring’s dependency injection and context management in JUnit 5 tests.
public class AuthControllerITest {    //Without @ExtendWith(), @Autowired won’t work inside test classes.

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper; // JSON converting

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("Integration test - /user/register -> User registration")
    @Test
    void givenValidMemberRequest_whenRegister_thenReturnSuccess() throws Exception {
        //GIVEN
        MemberRequest memberRequest = new MemberRequest("testuser", "password123");

        //WHEN & THEN
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("Integration test - /user/login -> Login with valid credentials")
    @Test
    void givenValidMemberRequest_whenLogin_thenReturnJwtToken() throws Exception {
        //GIVEN: first register a user
        MemberRequest registerRequest = new MemberRequest("testuser", "password123");
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        //WHEN & THEN: login with the same credetials
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.roles").isArray());

    }

    @DisplayName("Integration test - Access protected endpoint without authentication")
    @Test
    void givenNoAuthToken_whenAccessProtectedEndpoint_thenReturnUnauthorized() throws Exception {
        // WHEN & THEN
        mockMvc.perform(get("/user/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Integration test - Access protected endpoint with valid JWT")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authenticated user
    void givenAuthToken_whenAccessProtectedEndpoint_thenReturnUserDetails() throws Exception {
        // WHEN & THEN
        mockMvc.perform(get("/user/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

}
