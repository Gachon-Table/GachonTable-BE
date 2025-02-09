package site.gachontable.presentation.waiting.dto.request

import site.gachontable.presentation.shared.Table

data class RemoteWaitingRequest(
    val pubId: Int,
    val tableType: Table,
)
