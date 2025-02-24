package site.gachontable.presentation.waiting.dto.response

import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.type.Status
import site.gachontable.presentation.shared.DateTimeFormatters
import java.time.LocalDateTime
import java.util.UUID

data class WaitingHistoryResponse(
    val waitingId: UUID,
    val pubName: String,
    val status: Status,
    val enteredTime: String,
    val exitTime: String,
) {
    companion object {
        fun of(waiting: Waiting, exitTime: LocalDateTime?): WaitingHistoryResponse =
            WaitingHistoryResponse(
                waitingId = waiting.id!!,
                pubName = waiting.pub.pubName,
                status = waiting.waitingStatus,
                enteredTime = waiting.updatedAt!!.format(DateTimeFormatters.WITH_WEEKDAY),
                exitTime = exitTime?.format(DateTimeFormatters.WITH_WEEKDAY).toString()
            )
    }
}