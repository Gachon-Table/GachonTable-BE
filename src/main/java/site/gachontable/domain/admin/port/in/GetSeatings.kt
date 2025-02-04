package site.gachontable.domain.admin.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.response.SeatingsResponse

interface GetSeatings {
    fun execute(authDetails: AuthDetails): SeatingsResponse
}
