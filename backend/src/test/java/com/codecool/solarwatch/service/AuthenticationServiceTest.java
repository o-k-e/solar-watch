package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.JwtResponse;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

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

    @Mock
    private Authentication authentication;

    @Mock
    private User userDetails;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Member testMember;
    private MemberRequest testRequest;
    private Role userRole;

    @BeforeEach
    void setUp() {
        testMember = mock(Member.class);
        userRole = mock(Role.class);
        testRequest = new MemberRequest("testuser", "password123");
    }

    @DisplayName("Unit test - register() Successful registration")
    @Test
    void givenValidMemberRequest_whenRegister_thenReturnSuccess() {
        //GIVEN
        when(memberRepository.existsByUsername("testuser")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        //WHEN
        ResponseEntity<?> response = authenticationService.register(testRequest);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(OK);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @DisplayName("Unit test - register() Registration fails if username exists")
    @Test
    void givenExistingUsername_whenRegister_thenReturnBadRequest() {
        //GIVEN
        when(memberRepository.existsByUsername("testuser")).thenReturn(true);

        //WHEN
        ResponseEntity<?> response = authenticationService.register(testRequest);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @DisplayName("Unit test - login() Successful login returns JWT")
    @Test
    void givenValidCredentials_whenLogin_thenReturnJwtToken() {
        // GIVEN
        Authentication mockAuthentication = mock(Authentication.class);
        User mockUser = mock(User.class);
        // WHEN
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getAuthorities()).thenReturn(Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(jwtUtils.generateJwtToken(mockAuthentication)).thenReturn("mockedJwtToken");

        ResponseEntity<JwtResponse> response = authenticationService.login(testRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(Objects.requireNonNull(response.getBody()).jwt()).isEqualTo("mockedJwtToken");
    }

    @DisplayName("Unit test - login() Authentication fails for invalid credentials")
    @Test
    void givenInvalidCredentials_whenLogin_thenThrowException() {
        // GIVEN
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // WHEN & THEN
        try {
            authenticationService.login(testRequest);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Authentication failed");
        }
    }

    @DisplayName("Unit test - me() Fetch authenticated user details")
    @Test
    void givenAuthenticatedUser_whenMe_thenReturnMemberResponse() {
        //User data mocking
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getPassword()).thenReturn("encodedPassword");
        when(mockUser.getAuthorities()).thenReturn(Set.of(new SimpleGrantedAuthority("ROLE_USER")));


        // SecurityContext mocking
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication mockAuth = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        //WHEN
        MemberResponse response = authenticationService.me();

        //THEN
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getPassword()).isEqualTo("encodedPassword");
        assertThat(response.getRoles()).isEqualTo(mockUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));
    }
}
