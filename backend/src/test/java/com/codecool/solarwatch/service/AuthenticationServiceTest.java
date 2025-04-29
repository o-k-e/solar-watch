package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.JwtResponse;
import com.codecool.solarwatch.model.dto.response.MemberPublicResponse;
import com.codecool.solarwatch.model.dto.response.MemberResponse;
import com.codecool.solarwatch.model.entity.Member;
import com.codecool.solarwatch.model.entity.Role;
import com.codecool.solarwatch.repository.MemberRepository;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import com.codecool.solarwatch.security.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("JUnit test for register() when user is successfully created")
    void register_shouldCreateNewMember() {
        MemberRequest request = new MemberRequest("testuser", "password");

        when(memberRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        Member savedMember = new Member();
        savedMember.setUsername("testuser");
        savedMember.setPassword("encodedPassword");
        savedMember.setRoles(Set.of(role));
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberPublicResponse response = authenticationService.register(request);

        assertEquals("testuser", response.getUsername());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("JUnit test for register() when username already exists")
    void register_shouldThrowException_whenUsernameExists() {
        MemberRequest request = new MemberRequest("existingUser", "password");
        when(memberRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
    }

    @Test
    @DisplayName("JUnit test for login() when authentication is successful")
    void login_shouldReturnJwtResponse() {
        MemberRequest request = new MemberRequest("testuser", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        User user = new User("testuser", "encodedPassword", Collections.emptyList());
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");

        JwtResponse jwtResponse = authenticationService.login(request);

        assertEquals("testuser", jwtResponse.userName());
        assertEquals("fake-jwt-token", jwtResponse.jwt());
    }

    @Test
    @DisplayName("JUnit test for login() when authentication fails")
    void login_shouldThrowException_whenAuthenticationFails() {
        MemberRequest request = new MemberRequest("invalidUser", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.login(request));

        assertEquals("Authentication failed", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("JUnit test for me() when user is authenticated")
    void me_shouldReturnCurrentUserInfo() {
        User user = new User("currentuser", "encodedPassword", Collections.emptyList());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MemberResponse response = authenticationService.me();

        assertEquals("currentuser", response.getUsername());
        assertEquals("encodedPassword", response.getPassword());
    }
}