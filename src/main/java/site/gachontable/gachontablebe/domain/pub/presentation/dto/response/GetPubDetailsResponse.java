package site.gachontable.gachontablebe.domain.pub.presentation.dto.response;

import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.List;

public record GetPubDetailsResponse(
        PubInfo pub,
        List<MenuInfo> menu
) {

    public static GetPubDetailsResponse of(Pub pub, List<Menu> menuList) {

        return new GetPubDetailsResponse(
                PubInfo.from(pub),
                menuList.stream()
                        .map(MenuInfo::from)
                        .toList());
    }

    public record PubInfo(
            Integer pubId,
            List<String> thumbnails,
            String pubName,
            String oneLiner,
            String instagramUrl,
            Boolean studentCard,
            String menu,
            Integer waitingCount,
            Boolean openStatus,
            Boolean waitingStatus
    ) {
        public static PubInfo from(Pub pub) {
            return new PubInfo(
                    pub.getPubId(),
                    pub.getThumbnails(),
                    pub.getPubName(),
                    pub.getOneLiner(),
                    pub.getInstagramUrl(),
                    pub.getStudentCard(),
                    pub.getRepresentativeMenu(),
                    pub.getWaitingCount(),
                    pub.getOpenStatus(),
                    pub.getWaitingStatus());
        }
    }


    public record MenuInfo(
            Integer menuId,
            String menuName,
            String price,
            String oneLiner,
            String thumbnail
    ) {
        public static MenuInfo from(Menu menu) {
            return new MenuInfo(
                    menu.getMenuId(),
                    menu.getMenuName(),
                    menu.getPrice(),
                    menu.getOneLiner(),
                    menu.getThumbnail()
            );
        }
    }
}
