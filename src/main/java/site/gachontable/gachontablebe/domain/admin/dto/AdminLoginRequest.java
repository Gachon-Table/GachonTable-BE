package site.gachontable.gachontablebe.domain.admin.dto;

import lombok.Getter;

@Getter
public record AdminLoginRequest(String id, String password) {
}
