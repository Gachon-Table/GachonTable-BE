package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.LoginResponse;

public interface AdminLogin {
    LoginResponse execute(String id, String password);
}
