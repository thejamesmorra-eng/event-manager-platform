package dev.sorokin.eventnotificator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@Slf4j
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public Long getUserIdFromToken(String jwt) {
        return getClaims(jwt).get("userId", Long.class);
    }

    public String getRoleFromToken(String jwt) {
        return getClaims(jwt).get("role", String.class);
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
