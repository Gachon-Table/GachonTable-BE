package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;

public interface AdminLogin {
    JwtResponse execute(String id, String password);
}
