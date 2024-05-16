package site.gachontable.gachontablebe.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.user.domain.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(6);

    private final SecretKey secretKey;

    @Autowired
    public JwtProvider(@Value("${jwt.secret_key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String generateAccessToken(User user, Role role) {
        return generateToken(user, ACCESS_TOKEN_DURATION, role);
    }

    public String generateRefreshToken(User user, Role role) {
        return generateToken(user, REFRESH_TOKEN_DURATION, role);
    }

    public String generateToken(User user, Duration duration, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .subject(user.getUserName())
                .claim("uid", user.getUserId().toString())
                .claim("role", role.getRole())
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
