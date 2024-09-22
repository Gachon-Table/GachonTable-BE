package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;

import java.util.List;
import java.util.UUID;

import static site.gachontable.gachontablebe.domain.shared.DateTimeFormatters.WITH_WEEKDAY;

public record SeatingsResponse(List<SeatingResponse> seatings) {

    @Builder
    public record SeatingResponse(Integer tableId, Integer tableNum, String exitTime, UUID waitingId) {
        public static SeatingResponse from(Seating seating) {

            return SeatingResponse.builder()
                    .tableId(seating.getSeatingId())
                    .tableNum(seating.getSeatingNum())
                    .exitTime(seating.getExitTime().format(WITH_WEEKDAY))
                    .waitingId(seating.getWaiting().getWaitingId())
                    .build();
        }
    }
}
