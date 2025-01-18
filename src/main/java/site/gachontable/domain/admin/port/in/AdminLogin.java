package site.gachontable.domain.admin.port.in;

import site.gachontable.presentation.admin.dto.response.AdminLoginResponse;

public interface AdminLogin {

    AdminLoginResponse execute(String id, String password);
}
