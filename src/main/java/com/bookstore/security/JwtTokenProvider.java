package com.bookstore.security;

import com.bookstore.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {

    private final JwtConfig JwtConfiguration;

    public JwtTokenProvider(JwtConfig JwtConfiguration) {
        this.JwtConfiguration = JwtConfiguration;
    }

    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private long expiration;


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JwtConfiguration.getSecret().getBytes());
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfiguration.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}