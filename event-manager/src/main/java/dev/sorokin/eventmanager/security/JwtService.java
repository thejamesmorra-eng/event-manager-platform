package dev.sorokin.eventmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtService(SecretKey secretKey, @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
    }

    public String generateToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        return getClaims(jwt).getSubject();
    }

    public boolean isTokenValid(String jwt) {
        try {
            getClaims(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Token expired: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Invalid signature — possible attack: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
