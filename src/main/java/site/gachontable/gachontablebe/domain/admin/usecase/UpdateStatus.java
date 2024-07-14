package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.UpdateStatusRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

public interface UpdateStatus {
    RegisterResponse execute(AuthDetails authDetails, UpdateStatusRequest request);
}
