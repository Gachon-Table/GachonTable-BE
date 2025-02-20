package site.gachontable.infra.biztalk.event

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import site.gachontable.infra.biztalk.SendBiztalk

@Component
class BiztalkEventHandler(
    private val sendBiztalk: SendBiztalk
) {
    @Async
    @TransactionalEventListener
    fun execute(event: SentBiztalkEvent) {
        sendBiztalk.execute(event.templateCode, event.userTel, event.variables)
    }
}
