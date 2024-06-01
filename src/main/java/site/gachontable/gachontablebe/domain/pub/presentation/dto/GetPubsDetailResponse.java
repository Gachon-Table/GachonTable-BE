package site.gachontable.gachontablebe.domain.pub.presentation.dto;

import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.List;

public record GetPubsDetailResponse(
        PubInfo pub,
        List<MenuInfo> menu
) {

    public static GetPubsDetailResponse from(Pub pub, List<Menu> menuList) {

        return new GetPubsDetailResponse(
                PubInfo.of(pub),
                menuList.stream()
                        .map(MenuInfo::of)
                        .toList());
    }

    public record PubInfo(
            Integer pubId,
            String imageUrl,
            String pubName,
            String onLiner,
            Boolean studentCard,
            String menu,
            Integer queueing
    ) {
        public static PubInfo of(Pub pub) {
            return new PubInfo(
                    pub.getPubId(),
                    pub.getPubThumbnail(),
                    pub.getPubName(),
                    pub.getOneLiner(),
                    pub.getStudentCard(),
                    pub.getRepresentativeMenu(),
                    pub.getWaitingQueue()
                            .stream()
                            .filter(Waiting::isWaiting)
                            .toList()
                            .size());
        }
    }


    public record MenuInfo(
            String menuName,
            Integer price,
            String oneLiner
    ) {
        public static MenuInfo of(Menu menu) {
            return new MenuInfo(
                    menu.getMenuName(),
                    menu.getPrice(),
                    menu.getOneLiner()
            );
        }
    }
}
