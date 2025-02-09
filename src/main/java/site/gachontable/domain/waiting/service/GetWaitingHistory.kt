package site.gachontable.domain.waiting.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.seating.exception.SeatingNotFoundException
import site.gachontable.domain.seating.port.out.SeatingRepository
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.response.WaitingHistoryResponse

@Service
class GetWaitingHistory(
    private val waitingRepository: WaitingRepository,
    private val seatingRepository: SeatingRepository,
) {
    @Transactional(readOnly = true)
    fun execute(authDetails: AuthDetails): MutableList<WaitingHistoryResponse> {
        val tel: String = authDetails.tel

        val waitings: MutableList<Waiting> = waitingRepository
            .findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(
                tel, mutableListOf(Status.ENTERED, Status.CANCELED)
            )

        return waitings.stream()
            .map<WaitingHistoryResponse> { waiting: Waiting ->
                if (waiting.waitingStatus == Status.ENTERED) {
                    val exitTime = seatingRepository.findExitTimeByWaiting(waiting)
                        .orElse(throw SeatingNotFoundException())
                    return@map WaitingHistoryResponse.of(waiting, exitTime)
                }
                WaitingHistoryResponse.of(waiting, null)
            }.toList()
    }
}
