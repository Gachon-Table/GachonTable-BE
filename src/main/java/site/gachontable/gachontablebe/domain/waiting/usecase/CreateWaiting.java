package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.WaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;

public interface CreateWaiting {
    WaitingResponse execute(User user, Integer pubId, WaitingRequest request);
}
