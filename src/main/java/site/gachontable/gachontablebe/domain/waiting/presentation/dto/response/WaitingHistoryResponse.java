package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WaitingHistoryResponse(
        UUID waitingId,
        String pubName,
        Status status,
        LocalDateTime enteredTime,
        LocalDateTime exitTime) {

    public static WaitingHistoryResponse of(Waiting waiting, Seating seating) {
        return WaitingHistoryResponse.builder()
                .waitingId(waiting.getWaitingId())
                .pubName(waiting.getPub().getPubName())
                .status(waiting.getWaitingStatus())
                .enteredTime(waiting.getUpdatedAt())
                .exitTime(seating.getExitTime())
                .build();
    }
}
