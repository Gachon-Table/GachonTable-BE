package site.gachontable.infra.security.jwt.dto;

public record JwtResponse(String accessToken,
                          String refreshToken) {
}
