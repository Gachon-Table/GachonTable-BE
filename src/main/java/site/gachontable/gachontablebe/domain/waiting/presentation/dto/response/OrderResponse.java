package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;

@Builder
public record OrderResponse(Long waitingId,
                            String pubName,
                            String orderStatus,
                            Integer order) {

    public static OrderResponse of(Long waitingId,
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
