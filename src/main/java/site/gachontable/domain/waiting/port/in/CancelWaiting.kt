package site.gachontable.domain.waiting.port.`in`

import site.gachontable.presentation.waiting.dto.request.CancelRequest
import site.gachontable.presentation.waiting.dto.response.WaitingResponse

interface CancelWaiting {
    fun execute(request: CancelRequest, lockKey: String): WaitingResponse
}
