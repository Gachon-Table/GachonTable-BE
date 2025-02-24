package site.gachontable.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.member.exception.UserNotFoundException
import site.gachontable.domain.member.port.`in`.UserLogin
import site.gachontable.domain.member.port.out.UserRepository
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.infra.security.jwt.dto.JwtResponse
import site.gachontable.presentation.shared.Role
import site.gachontable.presentation.shared.exception.PasswordNotMatchException

@Service
class UserLoginImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) : UserLogin {
    override fun execute(id: String, password: String): JwtResponse {
        val user = userRepository.findByUsername(id)
            ?: throw UserNotFoundException()

        validatePassword(password, user)

        val accessToken = jwtProvider.generateAccessToken(user.userId, user.userTel, Role.ROLE_USER)
        val refreshToken = generateRefreshToken(user)

        return JwtResponse(accessToken, refreshToken)
    }

    private fun validatePassword(password: String, user: User) {
        if (!passwordEncoder.matches(password, user.password)) {
            throw PasswordNotMatchException()
        }
    }

    private fun generateRefreshToken(user: User): String {
        var refreshToken = user.refreshToken
        if (refreshToken == null || !jwtProvider.isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(user.userId, user.username, Role.ROLE_USER)
            updateRefreshToken(user, refreshToken)
        }
        return refreshToken
    }

    private fun updateRefreshToken(user: User, refreshToken: String) {
        user.updateRefreshToken(refreshToken)
        userRepository.save(user)
    }
}
