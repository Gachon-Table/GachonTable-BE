package site.gachontable.domain.admin.port.in;

import site.gachontable.presentation.admin.dto.request.AdminRegisterRequest;
import site.gachontable.domain.shared.dto.response.RegisterResponse;

public interface AdminRegister {

    RegisterResponse execute(AdminRegisterRequest request);
}
