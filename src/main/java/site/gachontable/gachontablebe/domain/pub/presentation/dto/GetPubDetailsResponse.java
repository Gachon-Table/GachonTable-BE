package site.gachontable.gachontablebe.domain.pub.presentation.dto;

import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

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
            String imageUrl,
            String pubName,
            String onLiner,
            Boolean studentCard,
            String menu,
            Long queueing
    ) {
        public static PubInfo from(Pub pub) {
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
                            .count());
        }
    }


    public record MenuInfo(
            String menuName,
            Integer price,
            String oneLiner
    ) {
        public static MenuInfo from(Menu menu) {
            return new MenuInfo(
                    menu.getMenuName(),
                    menu.getPrice(),
                    menu.getOneLiner()
            );
        }
    }
}
