package site.gachontable.domain.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.`in`.GetSeatings
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.seating.domain.Seating
import site.gachontable.domain.seating.port.out.SeatingRepository
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.response.SeatingsResponse
import site.gachontable.presentation.admin.dto.response.SeatingsResponse.SeatingResponse
import java.time.LocalDateTime

@Service
class GetSeatingsImpl(
    private val adminRepository: AdminRepository,
    private val seatingRepository: SeatingRepository,
) : GetSeatings {
    @Transactional(readOnly = true)
    override fun execute(authDetails: AuthDetails): SeatingsResponse {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())
            .pub

        return SeatingsResponse(
            seatingRepository.findAllByPubAndExitTimeAfterOrderByExitTime(pub, LocalDateTime.now())
                .stream()
                .map<SeatingResponse> { seating -> Seating.toSeatingResponse(seating) }
                .toList()
        )
    }
}
