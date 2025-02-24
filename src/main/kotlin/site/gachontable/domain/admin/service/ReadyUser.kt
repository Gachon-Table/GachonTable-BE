package site.gachontable.domain.admin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.biztalk.SendBiztalk
import java.util.*

@Service
class ReadyUser(
    private val waitingRepository: WaitingRepository,
    private val sendBiztalk: SendBiztalk,

    @Value("\${biztalk.templateId.ready}")
    private val templateCode: String,
) {
    fun execute(pub: Pub) {
        val waitings: MutableList<Waiting> = waitingRepository.findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
            pub, mutableListOf<Status>(Status.WAITING, Status.AVAILABLE)
        )

        if (waitings.size < 3) {
            return
        }

        val waiting: Waiting = waitings[2]

        val variables = HashMap<String, String>()
        variables.put("#{pub}", pub.pubName)
        variables.put("#{username}", waiting.user.username)
        variables.put("#{waitingId}", waiting.id.toString())

        sendBiztalk.execute(templateCode, waiting.tel, variables)
    }
}
