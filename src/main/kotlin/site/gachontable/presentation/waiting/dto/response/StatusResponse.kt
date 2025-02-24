package site.gachontable.presentation.waiting.dto.response

import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.presentation.shared.DateTimeFormatters
import site.gachontable.presentation.shared.Table
import java.util.*

data class StatusResponse(
    val waitingId: UUID,
    val pubName: String,
    val orderStatus: String,
    val order: Int,
    val createdAt: String,
    val tableType: Table,
) {
    companion object {
        fun of(waiting: Waiting, pub: Pub, order: Int): StatusResponse =
            StatusResponse(
                waitingId = waiting.id!!,
                pubName = pub.pubName,
                orderStatus = waiting.waitingStatus.statusKo,
                order = order,
                createdAt = waiting.createdAt!!.format(DateTimeFormatters.WITH_WEEKDAY),
                tableType = waiting.tableType
            )
    }
}