package site.gachontable.domain.member.port.`in`

import site.gachontable.presentation.shared.dto.response.RegisterResponse

interface UserRegister {
    fun execute(username: String, password: String, tel: String): RegisterResponse
}
