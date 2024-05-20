package site.gachontable.gachontablebe.domain.pub.dto;

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
        Integer queueing
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
                .queueing(pub.getWaitingQueue().size())
                .build();
    }

}
