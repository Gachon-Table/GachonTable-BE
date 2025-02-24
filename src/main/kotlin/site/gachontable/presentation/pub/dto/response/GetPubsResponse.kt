package site.gachontable.presentation.pub.dto.response

import site.gachontable.domain.pub.domain.Pub

data class GetPubsResponse(
    val pubId: Long,
    val thumbnails: MutableList<String>,
    val pubName: String,
    val oneLiner: String,
    val waitingCount: Int,
) {
    companion object {
        fun from(pub: Pub, thumbnails: MutableList<String>): GetPubsResponse {
            return GetPubsResponse(
                pub.id!!,
                thumbnails,
                pub.pubName,
                pub.oneLiner,
                pub.waitingCount
            )
        }
    }
}
