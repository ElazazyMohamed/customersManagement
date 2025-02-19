package com.elazazy.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Date;
import java.time.Instant;

@Service
public class JWTUtil {
    private static final String SECRET_KEY =
            "foobar_123456789_foobar_123456789_foobar_123456789_foobar_123456789";

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, Map<String, Object> claims) {
        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("elazazy")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(15, ChronoUnit.DAYS)
                        )
                )
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private Key getSingingKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }
}
