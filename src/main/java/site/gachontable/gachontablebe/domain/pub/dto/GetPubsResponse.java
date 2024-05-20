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

    public static GetPubsResponse of(Pub pub) {
        return new GetPubsResponse(
                pub.getPubId(),
                pub.getPubThumbnail(),
                pub.getPubName(),
                pub.getOneLiner(),
                pub.getStudentCard(),
                pub.getRepresentativeMenu(),
                pub.getWaitingQueue().stream()
                        .filter(Waiting::isWaiting)
                        .toList()
                        .size(),
                pub.getOpenStatus()
        );

    }

}
