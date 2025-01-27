package site.gachontable.domain.waiting.port.in;

import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.presentation.waiting.dto.response.StatusResponse;

import java.util.List;

public interface GetStatus {

    List<StatusResponse> execute(AuthDetails authDetails);
}
