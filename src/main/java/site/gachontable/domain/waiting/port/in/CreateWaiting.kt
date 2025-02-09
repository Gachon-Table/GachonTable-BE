package site.gachontable.domain.waiting.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.request.RemoteWaitingRequest
import site.gachontable.presentation.waiting.dto.response.WaitingResponse

interface CreateWaiting {
    fun execute(
        authDetails: AuthDetails, request: RemoteWaitingRequest, lockKey: String,
    ): WaitingResponse
}
