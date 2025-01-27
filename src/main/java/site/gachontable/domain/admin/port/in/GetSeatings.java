package site.gachontable.domain.admin.port.in;

import site.gachontable.presentation.admin.dto.response.SeatingsResponse;
import site.gachontable.infra.security.principal.AuthDetails;

public interface GetSeatings {

    SeatingsResponse execute(AuthDetails authDetails);
}
