package site.gachontable.domain.admin.port.`in`

import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.PubManageRequest

interface ManagePub {
    fun execute(authDetails: AuthDetails, request: PubManageRequest): String
}
