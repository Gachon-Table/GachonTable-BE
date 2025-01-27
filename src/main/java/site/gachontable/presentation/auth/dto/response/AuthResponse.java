package site.gachontable.presentation.auth.dto.response;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           String username) {
}
