package site.gachontable.domain.waiting.service

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import site.gachontable.domain.admin.service.ReadyUser
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.WaitingNotFoundException
import site.gachontable.domain.waiting.port.`in`.CancelWaiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.biztalk.event.SentBiztalkEvent
import site.gachontable.infra.redis.RedissonLock
import site.gachontable.presentation.waiting.dto.request.CancelRequest
import site.gachontable.presentation.waiting.dto.response.WaitingResponse
import java.util.*

@Service
@RequiredArgsConstructor
class CancelWaitingImpl(
    private val waitingRepository: WaitingRepository,
    private val readyUser: ReadyUser,
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${biztalk.templateId.cancel}")
    private val templateCode: String,
) : CancelWaiting {
    @RedissonLock(key = "#lockKey")
    override fun execute(request: CancelRequest, lockKey: String): WaitingResponse {
        val waiting: Waiting = waitingRepository.findById(request.waitingId)
            .orElse(throw WaitingNotFoundException())
        val pub: Pub = waiting.pub

        val top3Waitings: MutableList<Waiting> = getTop3Waitings(pub)

        waiting.cancel()
        waiting.pub.decreaseWaitingCount()

        val variables = HashMap<String, String>()
        variables.put("#{pub}", pub.pubName)
        eventPublisher.publishEvent(
            SentBiztalkEvent.of(templateCode, waiting.tel, variables)
        )

        if (isWaitingIn(waiting, top3Waitings)) {
            readyUser.execute(pub)
        }

        return WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.message)
    }

    private fun getTop3Waitings(pub: Pub): MutableList<Waiting> {
        return waitingRepository
            .findTop3ByPubAndWaitingStatusInOrderByCreatedAtAsc(
                pub, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )
    }

    private fun isWaitingIn(waiting: Waiting, limitedWaitings: MutableList<Waiting>): Boolean {
        return limitedWaitings.contains(waiting)
    }
}
