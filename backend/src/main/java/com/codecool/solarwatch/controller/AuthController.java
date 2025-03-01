package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.JwtResponse;
import com.codecool.solarwatch.model.dto.response.MemberResponse;
import com.codecool.solarwatch.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class MemberController {

    private final AuthenticationService authenticationService;
//    private final PasswordEncoder encoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtils jwtUtils;


    public MemberController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
//        this.encoder = encoder;
//        this.authenticationManager = authenticationManager;
//        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody MemberRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @GetMapping("/me")
//    @PreAuthorize("hasRole('USER')")
    public MemberResponse me() {
        return authenticationService.me();

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return "Hello " + user.getUsername();
    }


}