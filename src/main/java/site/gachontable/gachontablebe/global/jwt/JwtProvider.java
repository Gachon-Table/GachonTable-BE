package site.gachontable.gachontablebe.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(6);

    private final SecretKey secretKey;

    @Autowired
    public JwtProvider(@Value("${jwt.secret_key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public JwtResponse refreshAccessToken(String refreshToken) {
        validateToken(refreshToken);

        Claims claims = parseClaims(refreshToken);
        UUID uuid = UUID.fromString(claims.get("uid", String.class));
        String role = claims.get("role", String.class);

        String newAccessToken = generateAccessToken(uuid, claims.getSubject(), Role.valueOf(role));
        return new JwtResponse(newAccessToken, null);
    }

    public String generateAccessToken(UUID uuid, String tokenSubject, Role role) {
        return generateToken(uuid, tokenSubject, ACCESS_TOKEN_DURATION, role);
    }

    public String generateRefreshToken(UUID uuid, String tokenSubject, Role role) {
        return generateToken(uuid, tokenSubject, REFRESH_TOKEN_DURATION, role);
    }

    public String generateToken(UUID uuid, String tokenSubject, Duration duration, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .subject(tokenSubject)
                .claim("uid", uuid)
                .claim("role", role.getRole())
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(claims.get("role").toString()));

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }

    public UUID getUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Claims claims = parseClaims(token);
        return UUID.fromString(claims.get("uid", String.class));
    }
}
