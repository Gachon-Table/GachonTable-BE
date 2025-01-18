package site.gachontable.domain.waiting.port.in;

import site.gachontable.presentation.waiting.dto.request.CancelRequest;
import site.gachontable.presentation.waiting.dto.response.WaitingResponse;

public interface CancelWaiting {

    WaitingResponse execute(CancelRequest request, String lockKey);
}
