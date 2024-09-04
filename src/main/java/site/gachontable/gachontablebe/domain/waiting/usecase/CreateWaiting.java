package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;

public interface CreateWaiting {
    WaitingResponse execute(AuthDetails authDetails, RemoteWaitingRequest request, String lockKey);

//    WaitingResponse execute(AuthDetails authDetails, OnsiteWaitingRequest request, String lockKey);
}
