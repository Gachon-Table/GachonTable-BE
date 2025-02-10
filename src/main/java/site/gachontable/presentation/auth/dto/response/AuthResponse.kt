package site.gachontable.presentation.auth.dto.response

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
)
