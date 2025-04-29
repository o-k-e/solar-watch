package com.codecool.solarwatch.security.service;

import com.codecool.solarwatch.controller.AuthController;
import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.ErrorResponse;
import com.codecool.solarwatch.model.dto.response.JwtResponse;
import com.codecool.solarwatch.model.dto.response.MemberResponse;
import com.codecool.solarwatch.model.dto.response.SuccessResponse;
import com.codecool.solarwatch.model.entity.Member;
import com.codecool.solarwatch.model.entity.Role;
import com.codecool.solarwatch.repository.MemberRepository;
import com.codecool.solarwatch.repository.RoleRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtils jwtUtils;
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;


    @Autowired
    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> register(MemberRequest registerRequest)  {

        if (memberRepository.existsByUsername(registerRequest.username())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exists"));
        }

        try {
            Member member = new Member();
            member.setUsername(registerRequest.username());
            member.setPassword(passwordEncoder.encode(registerRequest.password()));

            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("USER").orElseThrow(
                    () -> new RuntimeException("Default role ROLE_USER not found in database"));
            roles.add(role);

            member.setRoles(roles);

            memberRepository.save(member);

            return ResponseEntity.ok(new SuccessResponse(true));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ErrorResponse("Error registering user"));
        }
    }

    public ResponseEntity<JwtResponse> login(MemberRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(jwt, username, roles));

    }

    public MemberResponse me() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setUsername(user.getUsername());
        memberResponse.setPassword(user.getPassword());
        memberResponse.setRoles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return memberResponse;
    }
}
