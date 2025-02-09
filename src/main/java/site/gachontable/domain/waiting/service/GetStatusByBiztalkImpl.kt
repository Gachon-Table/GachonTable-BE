package site.gachontable.domain.waiting.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.WaitingNotFoundException
import site.gachontable.domain.waiting.port.`in`.GetStatusByBiztalk
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.presentation.waiting.dto.response.StatusResponse
import java.util.*

@Service
@RequiredArgsConstructor
class GetStatusByBiztalkImpl(
    private val waitingRepository: WaitingRepository,
) : GetStatusByBiztalk {
    @Transactional(readOnly = true)
    override fun execute(waitingId: UUID): StatusResponse {
        val waiting: Waiting = waitingRepository.findById(waitingId)
            .orElse(throw WaitingNotFoundException())
        val pub: Pub = waiting.pub

        return StatusResponse.of(waiting, pub, getIndexOfWaiting(waiting, pub))
    }

    private fun getIndexOfWaiting(waiting: Waiting, pub: Pub): Int {
        if (waiting.waitingStatus == Status.CANCELED) {
            return -1
        }
        if (waiting.waitingStatus == Status.ENTERED) {
            return -2
        }

        val waitings: MutableList<Waiting> = waitingRepository
            .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                pub, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )

        return waitings.indexOf(waiting) + 1
    }
}
