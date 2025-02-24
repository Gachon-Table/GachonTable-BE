package site.gachontable.infra.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.infra.security.jwt.dto.JwtResponse
import site.gachontable.infra.security.jwt.exception.ExpiredTokenException
import site.gachontable.infra.security.jwt.exception.InvalidTokenException
import site.gachontable.infra.security.jwt.exception.MalformedTokenException
import site.gachontable.infra.security.jwt.exception.UnsupportedTokenException
import site.gachontable.infra.security.principal.AdminAuthDetailsService
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.infra.security.principal.UserAuthDetailsService
import site.gachontable.presentation.shared.Role
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Service
class JwtProvider(
    private val userAuthDetailsService: UserAuthDetailsService,
    private val adminAuthDetailsService: AdminAuthDetailsService,

    @Value("\${jwt.secret_key}")
    secretKey: String,
) {
    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey))

    @Transactional
    fun refreshAccessToken(refreshToken: String): JwtResponse {
        val claims = validateToken(refreshToken)
        val uuid = UUID.fromString(claims.get<String>("uid", String::class.java))
        val role = claims.get<String>("role", String::class.java)
        val newAccessToken = generateAccessToken(
            uuid, claims.subject, Role.valueOf(role)
        )

        return JwtResponse(newAccessToken, null)
    }

    fun generateAccessToken(
        uuid: UUID, tokenSubject: String, role: Role,
    ): String {
        return generateToken(
            uuid, tokenSubject, ACCESS_TOKEN_DURATION, role
        )
    }

    fun generateRefreshToken(
        uuid: UUID, tokenSubject: String, role: Role,
    ): String {
        return generateToken(
            uuid, tokenSubject, REFRESH_TOKEN_DURATION, role
        )
    }

    private fun generateToken(
        uuid: UUID, tokenSubject: String, duration: Duration, role: Role,
    ): String {
        val now = Date()
        val expiry = Date(now.time + duration.inWholeMilliseconds)

        return Jwts.builder()
            .subject(tokenSubject)
            .claim("uid", uuid)
            .claim("role", role.role)
            .expiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = validateToken(token)
        val authorities: MutableCollection<out GrantedAuthority> = mutableListOf<SimpleGrantedAuthority>(
            SimpleGrantedAuthority(claims["role"].toString())
        )

        return UsernamePasswordAuthenticationToken(getDetails(claims), "", authorities)
    }

    private fun getDetails(claims: Claims): AuthDetails {
        if (claims["role"] == Role.ROLE_ADMIN.role) {
            return this.adminAuthDetailsService.loadUserByUsername(claims.subject)
        }
        return this.userAuthDetailsService.loadUserByUsername(claims.subject)
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey).build()
            .parseSignedClaims(token)
            .getPayload()
    }

    fun validateToken(token: String): Claims {
        try {
            return parseClaims(token)
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException()
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        } catch (e: MalformedJwtException) {
            throw MalformedTokenException()
        } catch (e: UnsupportedJwtException) {
            throw UnsupportedTokenException()
        }
    }

    fun isValidToken(token: String): Boolean {
        return runCatching { validateToken(token) }
            .onFailure { e ->
                if (e is InvalidTokenException ||
                    e is ExpiredTokenException ||
                    e is MalformedTokenException ||
                    e is UnsupportedTokenException
                ) {
                    return false
                }
            }
            .isSuccess
    }

    companion object {
        private val ACCESS_TOKEN_DURATION: Duration = 7.days
        private val REFRESH_TOKEN_DURATION: Duration = 14.days
    }
}
