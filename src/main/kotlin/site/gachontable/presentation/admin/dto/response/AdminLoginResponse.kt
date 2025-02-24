package site.gachontable.presentation.admin.dto.response

data class AdminLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val pubId: Long,
)
