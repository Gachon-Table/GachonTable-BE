package site.gachontable.gachontablebe.domain.user.usecase;

import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;

public interface UserLogin {

    JwtResponse execute(String id, String password);
}
