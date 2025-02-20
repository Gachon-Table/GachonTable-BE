package site.gachontable.infra.security.principal

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import site.gachontable.domain.admin.domain.Admin
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.presentation.shared.Role

@Service
class AdminAuthDetailsService(
    private val adminRepository: AdminRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): AuthDetails {
        val admin: Admin = adminRepository.findByUsername(username)
            ?: throw AdminNotFoundException()

        return AuthDetails(
            admin.adminId, admin.username, Role.ROLE_ADMIN
        )
    }
}
