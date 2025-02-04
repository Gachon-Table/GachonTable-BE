package site.gachontable.domain.admin.port.out

import org.springframework.data.jpa.repository.JpaRepository
import site.gachontable.domain.admin.domain.Admin
import java.util.*

interface AdminRepository : JpaRepository<Admin, UUID> {
    fun findByUsername(username: String): Admin?
}
