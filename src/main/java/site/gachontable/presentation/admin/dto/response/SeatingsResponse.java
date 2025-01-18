package site.gachontable.presentation.admin.dto.response;

import lombok.Builder;
import site.gachontable.domain.seating.domain.Seating;
import site.gachontable.domain.shared.Table;

import java.util.List;
import java.util.UUID;

import static site.gachontable.domain.shared.DateTimeFormatters.WITH_WEEKDAY;

public record SeatingsResponse(List<SeatingResponse> seatings) {

    @Builder
    public record SeatingResponse(Integer seatingId,
                                  Integer seatingNum,
                                  Table tableType,
                                  String exitTime,
                                  UUID waitingId) {

        public static SeatingResponse from(Seating seating) {

            return SeatingResponse.builder()
                    .seatingId(seating.getSeatingId())
                    .seatingNum(seating.getSeatingNum())
                    .tableType(seating.getTableType())
                    .exitTime(seating.getExitTime().format(WITH_WEEKDAY))
                    .waitingId(seating.getWaiting().getWaitingId())
                    .build();
        }
    }
}
