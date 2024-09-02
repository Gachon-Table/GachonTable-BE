package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SeatingsResponse(List<SeatingResponse> seatings) {

    @Builder
    public record SeatingResponse(Integer tableNum, LocalDateTime exitTime, UUID waitingId) {
        public static SeatingResponse from(Seating seating) {
            return SeatingResponse.builder()
                    .tableNum(seating.getSeatingNum())
                    .exitTime(seating.getExitTime())
                    .waitingId(seating.getWaiting().getWaitingId())
                    .build();
        }
    }
}
