package site.gachontable.domain.admin.port.in;

import site.gachontable.presentation.admin.dto.request.UpdateStatusRequest;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.presentation.shared.dto.response.RegisterResponse;

public interface UpdateStatus {

    RegisterResponse executeForOpenStatus(AuthDetails authDetails, UpdateStatusRequest request);

    RegisterResponse executeForWaitingStatus(AuthDetails authDetails, UpdateStatusRequest request);
}
