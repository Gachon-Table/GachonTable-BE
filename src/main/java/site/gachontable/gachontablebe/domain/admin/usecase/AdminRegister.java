package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.shared.dto.response.TestRegisterResponse;

public interface AdminRegister {
    TestRegisterResponse execute(String username, String password, String tel);
}
