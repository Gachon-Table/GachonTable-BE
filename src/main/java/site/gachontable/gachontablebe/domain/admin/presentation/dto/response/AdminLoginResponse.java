package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

public record AdminLoginResponse(String accessToken, String refreshToken, Integer pubId) {
}
