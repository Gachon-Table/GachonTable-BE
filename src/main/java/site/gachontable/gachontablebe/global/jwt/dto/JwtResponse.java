package site.gachontable.gachontablebe.global.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record JwtResponse(String accessToken, String refreshToken) {
}
