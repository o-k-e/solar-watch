package com.codecool.solarwatch.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//Ez az osztály, JwtUtils, felelős a JWT tokenek kezeléséért az alkalmazásban.
//        Feladatai:
//        •	JWT token generálása bejelentkezéskor.
//        •	JWT token ellenőrzése, hogy érvényes-e.
//	      •	Felhasználónév kinyerése a JWT tokenből.
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${codecool.app.jwtSecret}") // @Value → Ez az érték a application.properties vagy application.yml fájlból jön.
    private String jwtSecret;       // A titkos kulcs a JWT token aláírásához.

    @Value("${codecool.app.jwtExpirationMs}")
    private int jwtExpirationMs; // A token lejárati ideje milliszekundumban.

    //     Ez a metódus felelős a JWT token létrehozásáért.
    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal(); // Lekérdezi a bejelentkezett felhasználót

//         Beállítja a token adatait es
//         Visszaadja a JWT token szövegként.
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // A token a felhasználónévhez kötődik
                .setIssuedAt(new Date())                  // A token mostani időpontban lett létrehozva.
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // A token lejár egy adott idő után.
                .signWith(key(), SignatureAlgorithm.HS256)  // Aláírja a tokent (signWith(key(), SignatureAlgorithm.HS256)) a titkos kulccsal.
                .compact();
    }

    //    Ez a metódus a titkos kulcsot állítja elő a JWT aláírásához.
//    Hogyan működik?
//            •	jwtSecret egy Base64-ben tárolt kulcs az application.properties-ben.
//	          •	Decoders.BASE64.decode(jwtSecret) → Ezt visszaalakítja bináris formába.
//            •	Keys.hmacShaKeyFor(...) → Egy HMAC-SHA kulcsot generál belőle.
//
//     Miért kell ez?
//          A JWT érvényességének ellenőrzéséhez és az aláírás hitelesítéséhez kell egy titkos kulcs.
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    //    Ez a metódus kiolvassa a felhasználónevet a JWT tokenből.
//    Hogyan működik?
//            1.	Ellenőrzi a token érvényességét.
//            2.	Kinyeri a “subject” mezőt, ami a felhasználónév.
//	          3.	Visszaadja a felhasználónevet.
//
//     Miért kell ez?
//          Ha egy felhasználó elküldi a tokent, ebből tudjuk meg a nevét, és így beazonosíthatjuk az adatbázisban.
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    //    Minden beérkező kérésnél ellenőrizzük a JWT tokent, és ha érvénytelen, a felhasználót nem engedjük tovább.
//
//     Hogyan működik?
//            1.	Megpróbálja kielemezni a JWT tokent.
//	          2.	Ha a token érvényes, visszaadja true-t.
//	          3.	Ha hiba van, naplózza, hogy miért érvénytelen, és visszaad false-t.
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken); //
            return true;
        } catch (MalformedJwtException e) { // A token hibásan van formázva.
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) { // A token lejárt.
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) { // A token nem támogatott.
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) { // A token üres vagy hibás.
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}