package site.gachontable.domain.member.port.out

import org.springframework.data.jpa.repository.JpaRepository
import site.gachontable.domain.member.domain.User
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?

    fun findByUserTel(tel: String): User?
}
