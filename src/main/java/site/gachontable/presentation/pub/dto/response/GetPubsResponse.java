package site.gachontable.presentation.pub.dto.response;

import lombok.Builder;
import site.gachontable.domain.pub.domain.Pub;

import java.util.List;

@Builder
public record GetPubsResponse(
        Integer pubId,
        List<String> thumbnails,
        String pubName,
        String oneLiner,
        Integer waitingCount) {

    public static GetPubsResponse from(Pub pub, List<String> thumbnails) {

        return new GetPubsResponse(
                pub.getPubId(),
                thumbnails,
                pub.getPubName(),
                pub.getOneLiner(),
                pub.getWaitingCount());
    }
}
