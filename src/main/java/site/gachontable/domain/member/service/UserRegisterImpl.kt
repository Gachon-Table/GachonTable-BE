package site.gachontable.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.member.port.`in`.UserRegister
import site.gachontable.domain.member.port.out.UserRepository
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.presentation.shared.Role
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@Service
class UserRegisterImpl(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserRegister {
    @Transactional
    override fun execute(username: String, password: String, tel: String): RegisterResponse {
        val user = createUser(username, password, tel)
        generateRefreshToken(user)

        return RegisterResponse(true, "유저 가입 성공")
    }

    private fun createUser(username: String, password: String, tel: String): User {
        val user = User.createForTest(username, passwordEncoder.encode(password), tel)
        userRepository.save(user)
        return user
    }

    private fun generateRefreshToken(user: User) {
        val refreshToken =
            jwtProvider.generateRefreshToken(user.userId, user.username, Role.ROLE_USER)
        updateUserRefreshToken(user, refreshToken)
    }

    private fun updateUserRefreshToken(user: User, refreshToken: String) {
        user.updateRefreshToken(refreshToken)
        userRepository.save(user)
    }
}
