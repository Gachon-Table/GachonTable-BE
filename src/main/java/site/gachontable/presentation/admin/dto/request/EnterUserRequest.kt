package site.gachontable.presentation.admin.dto.request

import java.util.*

data class EnterUserRequest(
    val waitingId: UUID,
    val seatingNum: Int,
) 