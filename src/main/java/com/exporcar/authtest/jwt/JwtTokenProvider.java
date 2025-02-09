package com.exporcar.authtest.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "9e976408bc5f199a3d4b023ad1e37153decb352bb642874392f664f09cd0292423425d89dd28adb865fad0eeae9da16b0873eab806fe1cc92c0936b4b3e0bd69";
    private static final long ACCESS_TOKEN_EXPIRY = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000; // 7일

    public String generateToken(String username, JwtType type) {
        long expiry = (type == JwtType.ACCESS_TOKEN) ? ACCESS_TOKEN_EXPIRY : REFRESH_TOKEN_EXPIRY;

        return Jwts.builder()
                .setIssuer("exporcar")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
