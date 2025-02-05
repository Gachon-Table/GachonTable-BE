package site.gachontable.domain.waiting.port.in;

import site.gachontable.presentation.waiting.dto.response.StatusResponse;

import java.util.UUID;

public interface GetStatusByBiztalk {

    StatusResponse execute(UUID waitingId);
}
