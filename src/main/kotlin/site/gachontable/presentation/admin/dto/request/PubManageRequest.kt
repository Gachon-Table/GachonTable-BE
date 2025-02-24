package site.gachontable.presentation.admin.dto.request

data class PubManageRequest(
    val thumbnails: MutableList<String>,
    val menuRequests: MutableList<MenuRequest>,
) {
    data class MenuRequest(
        val menuId: Int,
        val thumbnail: String,
        val menuName: String,
        val price: String,
        val oneLiner: String,
    )
}
