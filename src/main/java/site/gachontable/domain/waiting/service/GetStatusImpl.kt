package site.gachontable.domain.waiting.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.port.`in`.GetStatus
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.response.StatusResponse

@Service
class GetStatusImpl(
    private val waitingRepository: WaitingRepository,
) : GetStatus {
    @Transactional(readOnly = true)
    override fun execute(authDetails: AuthDetails): MutableList<StatusResponse> {
        val tel: String = authDetails.tel

        val waitings: MutableList<Waiting> = waitingRepository
            .findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(
                tel, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )

        return waitings.stream()
            .map<StatusResponse> { waiting: Waiting ->
                getStatusResponse(waiting, getWaitingsInPubFrom(waiting))
            }.toList()
    }

    private fun getWaitingsInPubFrom(waiting: Waiting): MutableList<Waiting> {
        return waitingRepository
            .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                waiting.pub, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )
    }

    private fun getStatusResponse(waiting: Waiting, waitings: MutableList<Waiting>): StatusResponse {
        return StatusResponse.of(
            waiting, waiting.pub, waitings.indexOf(waiting) + 1
        )
    }
}
