package site.gachontable.domain.admin.service

import org.springframework.stereotype.Service
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.exception.SeatingNumAlreadyExistsException
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.exception.PubMismatchException
import site.gachontable.domain.seating.domain.Seating
import site.gachontable.domain.seating.port.out.SeatingRepository
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.exception.WaitingNotFoundException
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.redis.RedissonLock
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.EnterUserRequest
import java.time.LocalDateTime

@Service
class EnterUser(
    private val waitingRepository: WaitingRepository,
    private val adminRepository: AdminRepository,
    private val readyUser: ReadyUser,
    private val seatingRepository: SeatingRepository,
) {
    @RedissonLock(key = "#lockKey")
    fun execute(
        authDetails: AuthDetails, request: EnterUserRequest, lockKey: String,
    ): String {
        val waiting: Waiting = waitingRepository.findById(request.waitingId)
            .orElse(throw WaitingNotFoundException())
        val pub: Pub = waiting.pub

        checkPubMatches(authDetails, pub)

        waiting.enter()
        pub.decreaseWaitingCount()
        createSeating(pub, waiting, request.seatingNum)

        readyUser.execute(pub)

        return SuccessCode.ENTERED_SUCCESS.message
    }

    private fun checkPubMatches(authDetails: AuthDetails, pub: Pub) {
        val admin = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())

        if (pub != admin.pub) {
            throw PubMismatchException()
        }
    }

    private fun createSeating(
        pub: Pub, waiting: Waiting, seatingNum: Int,
    ) {
        checkSeatingExists(pub, seatingNum)

        val seating: Seating = Seating.create(
            seatingNum,
            waiting.tableType,
            LocalDateTime.now().plusMinutes(pub.minutes.toLong()),
            pub,
            waiting,
            waiting.user
        )

        seatingRepository.save(seating)
    }

    private fun checkSeatingExists(pub: Pub, seatingNum: Int) {
        val seatingExists = seatingRepository
            .existsByPubAndSeatingNumAndExitTimeAfter(pub, seatingNum, LocalDateTime.now())

        if (seatingExists) {
            throw SeatingNumAlreadyExistsException()
        }
    }
}
