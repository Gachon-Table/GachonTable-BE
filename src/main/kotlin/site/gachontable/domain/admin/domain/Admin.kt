package site.gachontable.domain.admin.domain

import jakarta.persistence.*
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.infra.database.basetime.BaseTimeEntity

import java.util.UUID

@Entity(name = "admin")
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    val pub: Pub,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val adminPassword: String,

    @Column(columnDefinition = "char(16)", nullable = false)
    val adminTel: String,

    @Column
    var refreshToken: String? = null,
) : BaseTimeEntity() {
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    companion object {
        fun create(
            username: String, adminPassword: String, adminTel: String, pub: Pub,
        ): Admin {
            return Admin(
                pub = pub,
                username = username,
                adminPassword = adminPassword,
                adminTel = adminTel,
            )
        }
    }
}
