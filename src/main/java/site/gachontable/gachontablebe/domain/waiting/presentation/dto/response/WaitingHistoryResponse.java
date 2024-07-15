package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WaitingHistoryResponse(UUID waitingId, String pubName, Status status, LocalDateTime enteredTime) {
    public static WaitingHistoryResponse from(Waiting waiting) {
        return WaitingHistoryResponse.builder()
                .waitingId(waiting.getWaitingId())
                .pubName(waiting.getPub().getPubName())
                .status(waiting.getWaitingStatus())
                .enteredTime(waiting.getUpdatedAt())
                .build();
    }
}
