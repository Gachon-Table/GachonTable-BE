package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;

@Builder
public record WaitingHistoryResponse(String pubName, Status status, LocalDateTime enteredTime) {
    public static WaitingHistoryResponse from(Waiting waiting) {
        return WaitingHistoryResponse.builder()
                .pubName(waiting.getPub().getPubName())
                .status(waiting.getWaitingStatus())
                .build();
    }
}
