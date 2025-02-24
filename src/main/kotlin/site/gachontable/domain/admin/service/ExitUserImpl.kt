package site.gachontable.domain.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.admin.port.`in`.ExitUser
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.exception.PubMismatchException
import site.gachontable.domain.pub.exception.PubNotFoundException
import site.gachontable.domain.seating.domain.Seating
import site.gachontable.domain.seating.exception.SeatingNotFoundException
import site.gachontable.domain.seating.port.out.SeatingRepository
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.ExitUserRequest

@Service
class ExitUserImpl(
    private val seatingRepository: SeatingRepository,
    private val adminRepository: AdminRepository,
) : ExitUser {
    @Transactional
    override fun execute(authDetails: AuthDetails, request: ExitUserRequest): String {
        val seating: Seating = seatingRepository.findById(request.seatingId)
            .orElse(throw SeatingNotFoundException())

        checkPubMatches(authDetails, seating)

        seating.updateExitTime()

        return SuccessCode.EXIT_USER_SUCCESS.message
    }

    private fun checkPubMatches(authDetails: AuthDetails, seating: Seating) {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw PubNotFoundException())
            .pub

        if (seating.pub != pub) {
            throw PubMismatchException()
        }
    }
}
