package com.amitbashan.career_hub.components;

import com.amitbashan.career_hub.AppSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class AuthUtil {
    private final Base64.Decoder decoder;
    private final JwtParser jwtParser;
    private final String issuer = "CareerHub";

    public AuthUtil() {
        decoder = Base64.getDecoder();
        jwtParser = Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(issuer)
                .build();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = decoder.decode(AppSettings.AUTH_TOKEN_SECRET_B64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String username, boolean recruiterOrStudentFlag) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("userTypeFlag", recruiterOrStudentFlag)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(AppSettings.AUTH_TOKEN_VALID_DURATION_HOURS, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean verify(String token) {
        try {
            Jws<Claims> jws = jwtParser.parseSignedClaims(token);
            Date expirationDate = jws.getPayload().getExpiration();
            Date now = Date.from(Instant.now());

            return now.before(expirationDate);
        } catch (Exception e) {
            return false;
        }
    }
}

