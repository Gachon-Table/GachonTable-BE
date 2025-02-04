package site.gachontable.presentation.admin.dto.response;

public record AdminLoginResponse(String accessToken,
                                 String refreshToken,
                                 Integer pubId) {
}
