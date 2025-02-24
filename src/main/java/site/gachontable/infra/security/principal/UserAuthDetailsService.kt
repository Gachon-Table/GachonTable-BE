package site.gachontable.infra.security.principal

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.member.exception.UserNotFoundException
import site.gachontable.domain.member.port.out.UserRepository
import site.gachontable.presentation.shared.Role

@Service
class UserAuthDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(tel: String): AuthDetails {
        val user: User = userRepository.findByUserTel(tel)
            ?: throw UserNotFoundException()

        return AuthDetails(
            user.userId, user.userTel, Role.ROLE_USER
        )
    }
}
