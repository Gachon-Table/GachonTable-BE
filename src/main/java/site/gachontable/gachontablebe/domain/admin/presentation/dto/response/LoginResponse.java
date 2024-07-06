package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

public record LoginResponse(String accessToken, String refreshToken, Integer pubId) {
}
