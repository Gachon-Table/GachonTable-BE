package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StatusResponse(UUID waitingId,
                             String pubName,
                             String orderStatus,
                             Integer order) {

    public static StatusResponse of(UUID waitingId,
                                    String pubName,
                                    String orderStatus,
                                    Integer order) {
        return new StatusResponse(
                waitingId,
                pubName,
                orderStatus,
                order
        );
    }

}
