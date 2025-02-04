package site.gachontable.domain.admin.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import site.gachontable.domain.admin.domain.Admin
import site.gachontable.domain.admin.port.`in`.AdminRegister
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.domain.repository.PubRepository
import site.gachontable.domain.pub.exception.PubNotFoundException
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.presentation.admin.dto.request.AdminRegisterRequest
import site.gachontable.presentation.shared.Role
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@Service
class AdminRegisterImpl(
    private val passwordEncoder: PasswordEncoder,
    private val adminRepository: AdminRepository,
    private val pubRepository: PubRepository,
    private val jwtProvider: JwtProvider,
) : AdminRegister {
    override fun execute(request: AdminRegisterRequest): RegisterResponse {
        val pub: Pub = pubRepository.findById(request.pubId)
            .orElse(throw PubNotFoundException())

        val admin = Admin.create(
            request.username, passwordEncoder.encode(request.password), request.tel, pub
        )
        adminRepository.save(admin)

        generateRefreshToken(admin)

        return RegisterResponse(true, "어드민 가입 성공")
    }

    fun generateRefreshToken(admin: Admin) {
        val refreshToken = jwtProvider.generateRefreshToken(
            admin.adminId, admin.username, Role.ROLE_ADMIN
        )
        updateRefreshToken(admin, refreshToken)
    }

    private fun updateRefreshToken(admin: Admin, refreshToken: String) {
        admin.updateRefreshToken(refreshToken)
        adminRepository.save(admin)
    }
}
