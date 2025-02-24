package site.gachontable.domain.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import site.gachontable.domain.auth.domain.AccessToken
import site.gachontable.domain.auth.domain.KakaoProfile
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.member.port.out.UserRepository
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.presentation.auth.dto.response.AuthResponse
import site.gachontable.presentation.shared.Role

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private val clientId: String,

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private val clientSecret: String,

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private val redirectUri: String,
) {
    @Transactional
    fun getUserInfo(code: String): AuthResponse {
        val token = getToken(code)
        return getUserInfoFromToken(token)
    }

    private fun getToken(code: String): String {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val body: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
        body.add("grant_type", "authorization_code")
        body.add("client_id", clientId)
        body.add("redirect_uri", redirectUri)
        body.add("code", code)
        body.add("client_secret", clientSecret)

        val tokenRequest = HttpEntity<MultiValueMap<String, String>>(body, headers)

        val response: ResponseEntity<String> =
            restTemplate.postForEntity<String>(TOKEN_URI, tokenRequest, String::class.java)

        return AccessToken.from(response.getBody()!!).accessToken
    }

    fun getUserInfoFromToken(token: String?): AuthResponse {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.add("Authorization", "Bearer " + token)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val profileRequest = HttpEntity<MultiValueMap<String?, String>>(headers)

        val response: ResponseEntity<String> =
            restTemplate.postForEntity<String>(USER_INFO_URI, profileRequest, String::class.java)

        val user = getUser(KakaoProfile.from(response.getBody()!!))

        return createToken(user)
    }

    private fun getUser(kakaoProfile: KakaoProfile): User {
        val kakaoTel = kakaoProfile.tel
        val tel = kakaoTel.replace("+82 ", "0")
        val username = kakaoProfile.username

        return userRepository.findByUserTel(tel)
            ?: userRepository.save(User.create(username, tel))
    }

    private fun createToken(user: User): AuthResponse {
        val accessToken = jwtProvider.generateAccessToken(
            user.userId, user.userTel, Role.ROLE_USER
        )
        val refreshToken = generateRefreshToken(user)

        userRepository.save(user)

        return AuthResponse(accessToken, refreshToken, user.username)
    }

    private fun generateRefreshToken(user: User): String {
        var refreshToken = user.refreshToken
        if (refreshToken == null || !jwtProvider.isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(
                user.userId, user.userTel, Role.ROLE_USER
            )
            updateRefreshToken(user, refreshToken)
        }
        return refreshToken
    }

    private fun updateRefreshToken(user: User, refreshToken: String?) {
        user.updateRefreshToken(refreshToken)
        userRepository.save(user)
    }

    companion object {
        private const val USER_INFO_URI = "https://kapi.kakao.com/v2/user/me"
        private const val TOKEN_URI = "https://kauth.kakao.com/oauth/token"
    }
}
