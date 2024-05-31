package site.gachontable.gachontablebe.domain.user.usecase;

import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

public interface UserRegister {
    RegisterResponse execute(String username, String password, String tel);
}
