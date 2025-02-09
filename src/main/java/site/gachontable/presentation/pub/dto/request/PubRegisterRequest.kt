package site.gachontable.presentation.pub.dto.request

data class PubRegisterRequest(
    val pubName: String,
    val oneLiner: String,
    val instagramUrl: String,
    val minutes: Int,
    val menuUrl: String,
    val openStatus: Boolean,
    val waitingStatus: Boolean,
)
