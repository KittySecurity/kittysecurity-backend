package pl.edu.pk.student.kittysecurity.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.entity.User;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Integer JWT_TOKEN_VALIDITY_MS = 900_000; // 15 minutes
    private final String secretKey;

    public JwtService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            secretKey = Base64.getEncoder().encodeToString((keyGen.generateKey()).getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        Instant now = Instant.now();
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(String.valueOf(userId))
                .setHeaderParam("typ", "JWT")
                .setExpiration(Date.from(now.plusMillis(JWT_TOKEN_VALIDITY_MS)))
                .setIssuedAt(Date.from(now))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);

        if (userDetails instanceof User user) {
            return (userId.equals(String.valueOf(user.getUserId())) && !isTokenExpired(token));
        }

        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
