package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;

public record WaitingHistoryResponse(String pubName, Status status, LocalDateTime enteredTime) {
    public static WaitingHistoryResponse of(Waiting waiting) {
        return new WaitingHistoryResponse(
                waiting.getPub().getPubName(),
                waiting.getWaitingStatus(),
                waiting.getWaitingStatus().equals(Status.ENTERED) ? waiting.getUpdatedAt() : null
        );
    }
}
