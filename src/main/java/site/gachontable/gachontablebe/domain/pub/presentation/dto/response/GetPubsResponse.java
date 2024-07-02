package site.gachontable.gachontablebe.domain.pub.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

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
                pub.getWaitingCount(),
                pub.getOpenStatus()
        );

    }

}
