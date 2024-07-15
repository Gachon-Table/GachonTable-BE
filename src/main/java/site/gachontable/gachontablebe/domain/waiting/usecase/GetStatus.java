package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;

import java.util.List;

public interface GetStatus {
    List<StatusResponse> execute(AuthDetails authDetails);
}
