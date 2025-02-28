package com.codecool.solarwatch.security.configuration;

import com.codecool.solarwatch.security.jwt.AuthEntryPointJwt;
import com.codecool.solarwatch.security.jwt.AuthTokenFilter;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService; //Egy szolgáltatás, ami a felhasználók adatait kezeli (pl. adatbázisból).

    private final AuthEntryPointJwt unauthorizedHandler; // Egy osztály, ami kezeli a hibás bejelentkezéseket (pl. ha rossz jelszót írnak be).

    private final JwtUtils jwtUtils; // Egy segédosztály, ami kezeli a JWT tokent (létrehozás, ellenőrzés stb.).

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, AuthEntryPointJwt unauthorizedHandler, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    // Minden bejövő kérésnél ez a filter megnézi, hogy a felhasználó érvényes tokent küldött-e.
    public AuthTokenFilter authenticationJwtTokenFilter() { // AuthTokenFilter egy saját osztály, ami minden bejövő kérést ellenőriz, hogy van-e érvényes JWT token
        return new AuthTokenFilter(jwtUtils, userDetailsService); // Ehhez szüksége van: 1. jwtUtils → A JWT token kezelésére 2. userDetailsService → Hogy a tokenből kiolvassa a felhasználói adatokat.
    }

    //Ez biztosítja, hogy ha egy felhasználó be akar lépni, akkor:
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService); // 1. Lekérdezi az adatait az adatbázisból (userDetailsService).
        authProvider.setPasswordEncoder(passwordEncoder()); // 2. Ellenőrzi a jelszavát a titkosítással (passwordEncoder()).

        return authProvider;
    }

    // Az AuthenticationManager az a fő osztály, ami eldönti, hogy egy felhasználó be van-e lépve vagy sem.
    // Ha például valaki be akar lépni egy POST /login kérésben, akkor ez az osztály ellenőrzi a jelszót és eldönti, hogy sikeres-e a bejelentkezés.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception { //AuthenticationConfiguration egy Spring osztály, ami tartalmazza az összes szükséges beállítást.
        return authConfig.getAuthenticationManager();
    }

    // Ez az osztály felelős azért, hogy a jelszavakat:
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // Titkosítsa, mielőtt eltárolja.
//    }                                       // Összehasonlítsa, amikor a felhasználó bejelentkezik.


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("user/**").permitAll()
                                .requestMatchers("/solarwatch").hasRole("USER")
                                .requestMatchers("/city/read").hasRole("ADMIN")
                                .requestMatchers("/city/create").hasRole("ADMIN")
                                .requestMatchers("/city/update").hasRole("ADMIN")
                                .requestMatchers("/city/delete").hasRole("ADMIN")
                                .requestMatchers("/sunrise-sunset/read").hasRole("ADMIN")
                                .requestMatchers("/sunrise-sunset/create").hasRole("ADMIN")
                                .requestMatchers("/sunrise-sunset/update").hasRole("ADMIN")
                                .requestMatchers("/sunrise-sunset/delete").hasRole("ADMIN")
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()

                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
