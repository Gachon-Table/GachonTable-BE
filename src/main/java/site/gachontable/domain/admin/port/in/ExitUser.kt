package site.gachontable.domain.admin.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.ExitUserRequest

interface ExitUser {
    fun execute(authDetails: AuthDetails, request: ExitUserRequest): String
}
