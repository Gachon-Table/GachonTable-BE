package site.gachontable.infra.security.principal

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import site.gachontable.presentation.shared.Role
import java.util.*

class AuthDetails(
    val uuid: UUID,
    val tel: String,
    val role: Role,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority(role.role))

    override fun getPassword(): String? = null

    override fun getUsername(): String = tel
}
