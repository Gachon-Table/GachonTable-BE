package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;

@Builder
public record OrderResponse(String pubName,
                            String orderStatus,
                            Integer order) {

    public static OrderResponse of(String pubName,
                                   String orderStatus,
                                   Integer order) {
        return new OrderResponse(
                pubName,
                orderStatus,
                order
        );
    }

}
