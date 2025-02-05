package site.gachontable.domain.waiting.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.response.StatusResponse

interface GetStatus {
    fun execute(authDetails: AuthDetails): MutableList<StatusResponse>
}
