package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.shared.Table;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.UUID;

import static site.gachontable.gachontablebe.domain.shared.DateTimeFormatters.WITH_WEEKDAY;

@Builder
public record StatusResponse(UUID waitingId,
                             String pubName,
                             String orderStatus,
                             Integer order,
                             String createdAt,
                             Table tableType) {

    public static StatusResponse of(Waiting waiting, Pub pub, Integer order) {

        return StatusResponse.builder()
                .waitingId(waiting.getWaitingId())
                .pubName(pub.getPubName())
                .orderStatus(waiting.getWaitingStatus().getStatusKo())
                .order(order)
                .createdAt(waiting.getCreatedAt().format(WITH_WEEKDAY))
                .tableType(waiting.getTableType())
                .build();
    }
}