package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;

import java.util.UUID;

public interface GetStatusByBiztalk {
    StatusResponse execute(UUID waitingId);
}
