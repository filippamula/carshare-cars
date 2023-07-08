package com.project.carshare.cars.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "b2NjdXJjb29raWVzc3Rvb2Rwcm9iYWJseWNsb3RoZXNzdXBwZXJjYXJlZnVsbHljb3I=";

    public Authentication getAuthentication(String token){
        Claims claims = extractAllClaims(token);
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(
                Map.of("userId", claims.getSubject(),
                        "role", extractRole(token)),
                null,
                authorities);
    }

    public Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        var claims = extractAllClaims(token);
        return claims.get("role").toString();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
