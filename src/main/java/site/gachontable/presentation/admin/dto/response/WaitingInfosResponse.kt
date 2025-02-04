package site.gachontable.presentation.admin.dto.response

import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.type.Status
import site.gachontable.presentation.shared.Table
import java.time.LocalDateTime
import java.util.*

data class WaitingInfosResponse(
    val count: Int,
    val waitingInfos: MutableList<WaitingInfo>,
) {
    data class WaitingInfo(
        val username: String,
        val time: LocalDateTime,
        val tableType: Table,
        val tel: String,
        val waitingId: UUID,
        val waitingStatus: Status,
    ) {
        companion object {
            fun of(username: String, waiting: Waiting): WaitingInfo {
                return WaitingInfo(
                    username = username,
                    time = waiting.createdAt,
                    tableType = waiting.tableType,
                    tel = waiting.tel,
                    waitingId = waiting.waitingId,
                    waitingStatus = waiting.waitingStatus
                )
            }
        }
    }
}
