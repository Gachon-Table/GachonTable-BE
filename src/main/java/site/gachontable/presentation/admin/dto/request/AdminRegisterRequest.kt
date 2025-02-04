package site.gachontable.presentation.admin.dto.request

data class AdminRegisterRequest(
    val username: String,
    val password: String,
    val tel: String,
    val pubId: Int,
)
