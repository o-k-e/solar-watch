package com.codecool.solarwatch.security.jwt;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Spring Security automatikusan hívja ezt az osztályt, ha egy nem bejelentkezett vagy
// jogosulatlan felhasználó próbál hozzáférni egy API végponthoz.
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    //    Ez a metódus akkor fut le, ha valaki bejelentkezés nélkül
//    vagy helytelen jogosultságokkal próbál hozzáférni egy API végponthoz.
//    Hogyan működik?
//            1.	Bejön egy kérés (request), de a felhasználó nincs bejelentkezve
//                  vagy nincs joga az oldalhoz.
//            2.	Egy hibaüzenet naplózódik.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized"); // A kliens kap egy 401 státuszkódot.
        // A válasz tartalma: "Error: Unauthorized"
    }
}
