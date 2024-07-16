package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StatusResponse(UUID waitingId,
                             String pubName,
                             String orderStatus,
                             Integer order,
                             String createdAt) {

    public static StatusResponse of(UUID waitingId,
                                    String pubName,
                                    String orderStatus,
                                    Integer order,
                                    String createdAt) {
        return new StatusResponse(
                waitingId,
                pubName,
                orderStatus,
                order,
                createdAt
        );
    }

}
