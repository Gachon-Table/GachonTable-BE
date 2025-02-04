package site.gachontable.domain.admin.port.`in`

import site.gachontable.presentation.admin.dto.response.AdminLoginResponse

interface AdminLogin {
    fun execute(id: String, password: String): AdminLoginResponse
}
