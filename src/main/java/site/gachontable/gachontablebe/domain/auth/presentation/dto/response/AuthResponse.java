package site.gachontable.gachontablebe.domain.auth.presentation.dto.response;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           String username) {
}
