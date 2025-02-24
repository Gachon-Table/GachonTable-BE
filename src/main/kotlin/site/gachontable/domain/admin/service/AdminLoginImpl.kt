package site.gachontable.domain.admin.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import site.gachontable.domain.admin.domain.Admin
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.`in`.AdminLogin
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.presentation.admin.dto.response.AdminLoginResponse
import site.gachontable.presentation.shared.Role
import site.gachontable.presentation.shared.exception.PasswordNotMatchException

@Service
class AdminLoginImpl(
    private val jwtProvider: JwtProvider,
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
) : AdminLogin {
    override fun execute(username: String, password: String): AdminLoginResponse {
        val admin: Admin = adminRepository.findByUsername(username)
            ?: throw AdminNotFoundException()
        validatePassword(password, admin)

        val accessToken = jwtProvider.generateAccessToken(admin.id!!, admin.username, Role.ROLE_ADMIN)
        val refreshToken = generateRefreshToken(admin)
        val pubId = admin.pub.id!!

        return AdminLoginResponse(
            accessToken, refreshToken, pubId
        )
    }

    private fun validatePassword(password: String, admin: Admin) {
        if (!passwordEncoder.matches(password, admin.adminPassword)) {
            throw PasswordNotMatchException()
        }
    }

    private fun generateRefreshToken(admin: Admin): String {
        var refreshToken = admin.refreshToken
        if (refreshToken == null || !jwtProvider.isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(admin.id!!, admin.username, Role.ROLE_ADMIN)
            updateAdminRefreshToken(admin, refreshToken)
        }
        return refreshToken
    }

    private fun updateAdminRefreshToken(admin: Admin, refreshToken: String) {
        admin.updateRefreshToken(refreshToken)
        adminRepository.save(admin)
    }
}
