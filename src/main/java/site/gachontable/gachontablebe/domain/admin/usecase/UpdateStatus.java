package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.UpdateStatusRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

public interface UpdateStatus {
    RegisterResponse executeForOpenStatus(AuthDetails authDetails, UpdateStatusRequest request);

    RegisterResponse executeForWaitingStatus(AuthDetails authDetails, UpdateStatusRequest request);
}
