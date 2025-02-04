package site.gachontable.domain.member.port.`in`

import site.gachontable.infra.security.jwt.dto.JwtResponse

interface UserLogin {
    fun execute(id: String, password: String): JwtResponse
}
