package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.dto.request.MemberRequest;
import com.codecool.solarwatch.model.dto.response.JwtResponse;
import com.codecool.solarwatch.model.dto.response.MemberResponse;
import com.codecool.solarwatch.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final AuthenticationService authenticationService;


    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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