package site.gachontable.gachontablebe.domain.pub.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.List;

@Builder
public record GetPubsResponse(
        Integer pubId,
        List<String> thumbnails,
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
                pub.getThumbnails(),
                pub.getPubName(),
                pub.getOneLiner(),
                pub.getStudentCard(),
                pub.getRepresentativeMenu(),
                pub.getWaitingCount(),
                pub.getOpenStatus()
        );

    }

}
