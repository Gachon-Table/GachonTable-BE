package site.gachontable.infra.security.jwt.dto

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String?,
)
