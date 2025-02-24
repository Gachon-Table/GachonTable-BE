package site.gachontable.domain.member.domain

import jakarta.persistence.*
import site.gachontable.infra.database.basetime.BaseTimeEntity

import java.util.UUID

@Entity(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @Column(nullable = false)
    val username: String,

    @Column
    val password: String? = null,

    @Column(columnDefinition = "char(16)", nullable = false)
    val userTel: String,

    @Column
    var refreshToken: String? = null,
) : BaseTimeEntity() {
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    companion object {
        fun createForTest(
            username: String, userTel: String, password: String,
        ): User {
            return User(
                username = username,
                userTel = userTel,
                password = password,
            )
        }

        fun create(username: String, userTel: String): User {
            return User(
                username = username,
                userTel = userTel
            )
        }
    }
}
