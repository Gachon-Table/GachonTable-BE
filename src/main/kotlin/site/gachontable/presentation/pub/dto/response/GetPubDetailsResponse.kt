package site.gachontable.presentation.pub.dto.response

import site.gachontable.domain.menu.domain.Menu
import site.gachontable.domain.pub.domain.Pub

data class GetPubDetailsResponse(
    val pub: PubInfo,
    val menu: MutableList<MenuInfo>,
) {
    data class PubInfo(
        val pubId: Long,
        val thumbnails: MutableList<String>,
        val pubName: String,
        val oneLiner: String,
        val instagramUrl: String,
        val menuUrl: String,
        val waitingCount: Int,
        val openStatus: Boolean,
        val waitingStatus: Boolean,
    ) {
        companion object {
            fun of(pub: Pub, thumbnails: MutableList<String>): PubInfo {
                return PubInfo(
                    pub.id!!,
                    thumbnails,
                    pub.pubName,
                    pub.oneLiner,
                    pub.instagramUrl,
                    pub.menuUrl,
                    pub.waitingCount,
                    pub.openStatus,
                    pub.waitingStatus
                )
            }
        }
    }

    data class MenuInfo(
        val menuId: Long,
        val menuName: String,
        val price: String,
        val oneLiner: String,
        val thumbnail: String,
    ) {
        companion object {
            fun from(menu: Menu): MenuInfo {
                return MenuInfo(
                    menu.id!!,
                    menu.menuName,
                    menu.price,
                    menu.oneLiner,
                    menu.thumbnail
                )
            }
        }
    }

    companion object {
        fun of(pub: Pub, thumbnails: MutableList<String>, menus: MutableList<Menu>): GetPubDetailsResponse {
            return GetPubDetailsResponse(
                PubInfo.Companion.of(pub, thumbnails),
                menus.stream()
                    .map<MenuInfo> { menu: Menu ->
                        MenuInfo.from(menu)
                    }.toList()
            )
        }
    }
}
