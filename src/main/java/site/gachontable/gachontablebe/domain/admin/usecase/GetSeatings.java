package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;

public interface GetSeatings {

    SeatingsResponse execute(AuthDetails authDetails);
}
