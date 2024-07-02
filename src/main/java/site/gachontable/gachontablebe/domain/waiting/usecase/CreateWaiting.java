package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.OnsiteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;

public interface CreateWaiting {
    WaitingResponse execute(User user, RemoteWaitingRequest request);

    WaitingResponse execute(OnsiteWaitingRequest request);
}
