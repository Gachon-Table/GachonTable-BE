package site.gachontable.domain.admin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.WaitingNotFoundException
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.biztalk.event.SentBiztalkEvent
import site.gachontable.infra.redis.RedissonLock
import java.util.*

@Service
class AutoCancelUser(
    private val waitingRepository: WaitingRepository,
    private val readyUser: ReadyUser,
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${biztalk.templateId.forceCancel}")
    private val forceCancelTemplateCode: String,
) {
    @RedissonLock(key = "#lockKey")
    fun execute(
        waitingId: UUID, variables: HashMap<String, String>, lockKey: String,
    ) {
        val waiting: Waiting = waitingRepository.findById(waitingId)
            .orElse(throw WaitingNotFoundException())

        if (waiting.waitingStatus == Status.AVAILABLE) {
            waiting.cancel()
            waiting.pub.decreaseWaitingCount()

            eventPublisher.publishEvent(
                SentBiztalkEvent.of(
                    forceCancelTemplateCode, waiting.tel, variables
                )
            )

            readyUser.execute(waiting.pub)
        }
    }
}
