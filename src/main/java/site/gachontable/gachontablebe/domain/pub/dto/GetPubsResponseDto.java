package site.gachontable.gachontablebe.domain.pub.dto;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

@Builder
public record GetPubsResponseDto(
        Integer pubId,
        String url,
        String pubName,
        String oneLiner,
        Boolean studentCard,
        String menu,
        Integer queueing
) {
    public GetPubsResponseDto(Pub pub) {
        this(pub.getPubId(), pub.getPubThumbnail(), pub.getPubName(), pub.getOneLiner(), pub.getStudentCard(), pub.getRepresentativeMenu(), pub.getMenus().size());
    }
}
