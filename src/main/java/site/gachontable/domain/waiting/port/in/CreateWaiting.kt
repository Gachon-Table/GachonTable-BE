package site.gachontable.domain.waiting.port.in;

import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.presentation.waiting.dto.request.RemoteWaitingRequest;
import site.gachontable.presentation.waiting.dto.response.WaitingResponse;

public interface CreateWaiting {

    WaitingResponse execute(AuthDetails authDetails, RemoteWaitingRequest request, String lockKey);
}
