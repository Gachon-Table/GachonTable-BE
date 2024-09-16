package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.UUID;

import static site.gachontable.gachontablebe.domain.shared.DateTimeFormatters.WITH_WEEKDAY;

@Builder
public record WaitingHistoryResponse(
        UUID waitingId,
        String pubName,
        Status status,
        String enteredTime,
        String exitTime) {

    public static WaitingHistoryResponse of(Waiting waiting, Seating seating) {

        return WaitingHistoryResponse.builder()
                .waitingId(waiting.getWaitingId())
                .pubName(waiting.getPub().getPubName())
                .status(waiting.getWaitingStatus())
                .enteredTime(waiting.getUpdatedAt().format(WITH_WEEKDAY))
                .exitTime(seating.getExitTime().format(WITH_WEEKDAY))
                .build();
    }
}
