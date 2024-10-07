package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.AdminRegisterRequest;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

public interface AdminRegister {

    RegisterResponse execute(AdminRegisterRequest request);
}
