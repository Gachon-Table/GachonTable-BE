package site.gachontable.gachontablebe.domain.pub.dto;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import static site.gachontable.gachontablebe.domain.waiting.type.Status.WAITING;

@Builder
public record GetPubsResponse(
        Integer pubId,
        String url,
        String pubName,
        String oneLiner,
        Boolean studentCard,
        String menu,
        Integer queueing,
        Boolean isOpen
) {
    @Builder
    public static GetPubsResponse of(Pub pub) {
        return GetPubsResponse.builder()
                .pubId(pub.getPubId())
                .url(pub.getPubThumbnail())
                .pubName(pub.getPubName())
                .oneLiner(pub.getOneLiner())
                .studentCard(pub.getStudentCard())
                .menu(pub.getRepresentativeMenu())
                .queueing(pub.getWaitingQueue().stream().filter(Waiting::isWaiting).toList().size())
                .isOpen(pub.getOpenStatus())
                .build();
    }

}
