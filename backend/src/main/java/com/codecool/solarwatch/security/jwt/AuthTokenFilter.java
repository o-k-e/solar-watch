package com.codecool.solarwatch.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//	•	extends OncePerRequestFilter → Ez azt jelenti, hogy minden bejövő kéréshez egyszer lefut ez a szűrő.
//	•	Ez egy Spring Security filter, ami ellenőrzi az Authorization fejléct, hogy van-e benne JWT token.
// Mivel az alkalmazás stateless (nem használ session-t), minden kérésnél újra ellenőrizni kell, hogy a felhasználó be van-e jelentkezve.
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils; // jwtUtils → Ez az osztály felelős a JWT tokenek létrehozásáért és ellenőrzéséért.

    private final UserDetailsService userDetailsService; // userDetailsService → Ez az osztály felelős a felhasználói adatok betöltéséért az adatbázisból.


    // A JWT token nem tartalmazza a teljes felhasználói adatokat, ezért amikor egy kérés érkezik,
    // a tokenből ki kell olvasni a felhasználónevet, majd az adatbázisból betölteni az adatokat.
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    //Minden beérkező kérésnél ezt a metódust hívja meg a Spring Security, hogy ellenőrizze a felhasználót.
//    Ez a fő metódus, ami minden bejövő kérésnél lefut.
//            •	request → Ez a beérkező HTTP kérés.
//            •	response → Ez a válasz, amit a szerver küld vissza.
//            •	filterChain → A következő szűrő a láncban.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//      Ez a legfontosabb rész, ami ellenőrzi, hogy a kéréshez van-e érvényes JWT token.
//      Ha van, a felhasználót beállítja az alkalmazásban hitelesítettként.
        try {
            String jwt = parseJwt(request); // 	Megkeresi a JWT tokent a kérés fejléceiben.
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) { // Ellenőrzi, hogy a token érvényes-e.
                String username = jwtUtils.getUserNameFromJwtToken(jwt); //Kiolvassa a felhasználónevet a tokenből.

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Lekérdezi a felhasználói adatokat az adatbázisból.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());  // Létrehoz egy UsernamePasswordAuthenticationToken példányt.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication); // Beállítja a felhasználót a SecurityContextHolder-be, hogy a Spring Security tudja, hogy a felhasználó be van jelentkezve.
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e); // Ha hiba történik (pl. a token érvénytelen), akkor az exception kezelve van, és naplózva lesz.
        }

        filterChain.doFilter(request, response);
    }

    // JWT token kivágása a fejlécből (parseJwt())
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); // Kikeresi az Authorization fejlécet (request.getHeader("Authorization")).

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) { // Megnézi, hogy az tartalmaz-e egy JWT tokent.
            return headerAuth.substring(7); // Ha igen, levágja a "Bearer " szót, és visszaadja a tényleges JWT tokent.
        }                                              // A legtöbb API a JWT-t így küldi el a fejlécekben:
        //    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

        return null;
    }
}

