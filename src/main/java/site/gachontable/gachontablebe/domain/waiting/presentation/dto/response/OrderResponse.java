package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderResponse(UUID waitingId,
                            String pubName,
                            String orderStatus,
                            Integer order) {

    public static OrderResponse of(UUID waitingId,
                                   String pubName,
                                   String orderStatus,
                                   Integer order) {
        return new OrderResponse(
                waitingId,
                pubName,
                orderStatus,
                order
        );
    }

}
