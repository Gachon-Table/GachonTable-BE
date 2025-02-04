package site.gachontable.domain.admin.port.`in`

import site.gachontable.presentation.admin.dto.request.AdminRegisterRequest
import site.gachontable.presentation.shared.dto.response.RegisterResponse

interface AdminRegister {
    fun execute(request: AdminRegisterRequest): RegisterResponse
}
