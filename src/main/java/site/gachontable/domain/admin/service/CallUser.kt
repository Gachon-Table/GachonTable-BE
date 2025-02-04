package site.gachontable.domain.admin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import site.gachontable.domain.admin.domain.Admin
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.exception.PubMismatchException
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.WaitingNotFoundException
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.biztalk.SendBiztalk
import site.gachontable.infra.redis.RedissonLock
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.CallUserRequest
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Service
class CallUser(
    private val waitingRepository: WaitingRepository,
    private val adminRepository: AdminRepository,
    private val sendBiztalk: SendBiztalk,
    private val autoCancelUser: AutoCancelUser,

    @Value("\${biztalk.templateId.call}")
    private val callTemplateCode: String,
) {
    // TODO: Coroutine 변환 여부 판단
    private val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(8)

    @RedissonLock(key = "#lockKey")
    fun execute(
        authDetails: AuthDetails, request: CallUserRequest, lockKey: String,
    ): String {
        val waiting: Waiting = waitingRepository.findById(request.waitingId)
            .orElse(throw WaitingNotFoundException())
        val pub: Pub = waiting.pub

        checkPubMatches(authDetails, pub)

        waiting.toAvailable()

        val variables = HashMap<String, String>()
        variables.put("#{pub}", pub.pubName)
        sendBiztalk.execute(callTemplateCode, waiting.tel, variables)

        scheduleAutoCancel(request.waitingId, variables)

        return SuccessCode.USER_CALL_SUCCESS.message
    }


    private fun scheduleAutoCancel(waitingId: UUID, variables: HashMap<String, String>) {
        executorService.schedule(
            Runnable {
                autoCancelUser.execute(
                    waitingId, variables, "자동 취소"
                )
            }, 7, TimeUnit.MINUTES
        )
    }

    private fun checkPubMatches(authDetails: AuthDetails, pub: Pub) {
        val admin: Admin = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())

        if (pub != admin.pub) {
            throw PubMismatchException()
        }
    }
}
