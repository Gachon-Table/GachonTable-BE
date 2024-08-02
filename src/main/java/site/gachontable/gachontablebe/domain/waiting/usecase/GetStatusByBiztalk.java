package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.StatusRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;

public interface GetStatusByBiztalk {
    StatusResponse execute(StatusRequest request);
}
