package site.gachontable.gachontablebe.domain.user.usecase;

import site.gachontable.gachontablebe.domain.shared.dto.response.TestRegisterResponse;

public interface UserRegister {
    TestRegisterResponse execute(String username, String password, String tel);
}
