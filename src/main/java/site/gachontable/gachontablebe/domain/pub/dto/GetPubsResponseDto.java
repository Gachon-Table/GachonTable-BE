package site.gachontable.gachontablebe.domain.pub.dto;

import lombok.Builder;

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
}
