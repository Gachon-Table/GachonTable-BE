package site.gachontable.domain.admin.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.UpdateStatusRequest
import site.gachontable.presentation.shared.dto.response.RegisterResponse

interface UpdateStatus {
    fun executeForOpenStatus(authDetails: AuthDetails, request: UpdateStatusRequest): RegisterResponse

    fun executeForWaitingStatus(authDetails: AuthDetails, request: UpdateStatusRequest): RegisterResponse
}
