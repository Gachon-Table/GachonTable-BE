package site.gachontable.domain.waiting.port.`in`

import site.gachontable.presentation.waiting.dto.response.StatusResponse
import java.util.*

interface GetStatusByBiztalk {
    fun execute(waitingId: UUID): StatusResponse
}
