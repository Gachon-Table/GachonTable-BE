package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.AdminLoginResponse;

public interface AdminLogin {
    AdminLoginResponse execute(String id, String password);
}
