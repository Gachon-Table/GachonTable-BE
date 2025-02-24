package site.gachontable.presentation.admin.dto.response

import site.gachontable.domain.seating.domain.Seating
import site.gachontable.presentation.shared.DateTimeFormatters
import site.gachontable.presentation.shared.Table
import java.util.*

data class SeatingsResponse(val seatings: MutableList<SeatingResponse>) {
    data class SeatingResponse(
        val seatingId: Long,
        val seatingNum: Int,
        val tableType: Table,
        val exitTime: String,
        val waitingId: UUID,
    ) {
        companion object {
            fun from(seating: Seating): SeatingResponse {
                return SeatingResponse(
                    seatingId = seating.id!!,
                    seatingNum = seating.seatingNum,
                    tableType = seating.tableType,
                    exitTime = seating.exitTime.format(DateTimeFormatters.WITH_WEEKDAY),
                    waitingId = seating.waiting.id!!,
                )
            }
        }
    }
}
