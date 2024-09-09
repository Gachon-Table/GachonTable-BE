package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record SeatingsResponse(List<SeatingResponse> seatings) {

    @Builder
    public record SeatingResponse(Integer tableId, Integer tableNum, String exitTime, UUID waitingId) {
        public static SeatingResponse from(Seating seating) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            return SeatingResponse.builder()
                    .tableId(seating.getSeatingId())
                    .tableNum(seating.getSeatingNum())
                    .exitTime(seating.getExitTime().format(formatter))
                    .waitingId(seating.getWaiting().getWaitingId())
                    .build();
        }
    }
}
