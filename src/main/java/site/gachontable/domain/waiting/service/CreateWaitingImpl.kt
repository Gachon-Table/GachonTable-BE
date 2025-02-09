package site.gachontable.domain.waiting.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.member.exception.UserNotFoundException
import site.gachontable.domain.member.port.out.UserRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.domain.repository.PubRepository
import site.gachontable.domain.pub.exception.PubNotFoundException
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.UserWaitingLimitExcessException
import site.gachontable.domain.waiting.exception.WaitingAlreadyExistsException
import site.gachontable.domain.waiting.port.`in`.CreateWaiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Position
import site.gachontable.domain.waiting.type.Status
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.biztalk.event.SentBiztalkEvent
import site.gachontable.infra.redis.RedissonLock
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.request.RemoteWaitingRequest
import site.gachontable.presentation.waiting.dto.response.WaitingResponse
import java.util.*

@Service
class CreateWaitingImpl(
    private val pubRepository: PubRepository,
    private val waitingRepository: WaitingRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,

    @Value("\${biztalk.templateId.waiting}")
    private val templateCode: String,
) : CreateWaiting {
    @RedissonLock(key = "#lockKey")
    override fun execute(
        authDetails: AuthDetails,
        request: RemoteWaitingRequest,
        lockKey: String,
    ): WaitingResponse { // 원격 웨이팅
        val user = userRepository.findById(authDetails.uuid)
            .orElse(throw UserNotFoundException())
        val pub: Pub = pubRepository.findById(request.pubId)
            .orElse(throw PubNotFoundException())

        checkPreConditions(pub, user)

        val waiting: Waiting = waitingRepository.save(
            Waiting.create(
                Position.REMOTE, request.tableType, Status.WAITING, user.userTel, user, pub
            )
        )

        pub.increaseWaitingCount()

        eventPublisher.publishEvent(
            SentBiztalkEvent.of(
                templateCode,
                user.userTel,
                createVariables(user.username, pub, waiting, request.tableType.nameKo)
            )
        )

        return WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.message)
    }

    private fun checkPreConditions(pub: Pub, user: User) {
        // 신청할 주점의 상태 확인
        pub.checkStatus()

        // 해당 주점에 웨이팅 대기중이면 예외 처리
        checkDuplicatePubWaiting(pub, user)

        // 같은 번호로 3개 이상의 웨이팅이 대기중이면 예외 처리
        checkWaitingLimit(user)
    }

    private fun checkWaitingLimit(user: User) {
        if (waitingRepository.countByTelAndWaitingStatuses(
                user.userTel, Status.WAITING, Status.AVAILABLE
            ) >= WAITING_MAX_COUNT
        ) {
            throw UserWaitingLimitExcessException()
        }
    }

    private fun checkDuplicatePubWaiting(pub: Pub, user: User) {
        val duplicatePubWaitingExists = waitingRepository
            .existsByTelAndPubAndWaitingStatusIn(
                user.userTel, pub, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )

        if (duplicatePubWaitingExists) {
            throw WaitingAlreadyExistsException()
        }
    }

    private fun createVariables(
        username: String,
        pub: Pub,
        waiting: Waiting,
        tableType: String,
    ): HashMap<String, String> {
        val order: String = pub.waitingCount.toString()

        val variables = HashMap<String, String>()
        variables.put("#{username}", username)
        variables.put("#{pub}", pub.pubName)
        variables.put("#{headCount}", tableType)
        variables.put("#{order}", order)
        variables.put("#{waitingId}", waiting.waitingId.toString())

        return variables
    }

    companion object {
        private const val WAITING_MAX_COUNT = 3
    }
}
