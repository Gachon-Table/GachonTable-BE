package site.gachontable.domain.waiting.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.response.WaitingInfosResponse
import site.gachontable.presentation.admin.dto.response.WaitingInfosResponse.WaitingInfo

@Service
class GetWaitings(
    private val waitingRepository: WaitingRepository,
    private val adminRepository: AdminRepository,
) {
    @Transactional(readOnly = true)
    fun execute(authDetails: AuthDetails): WaitingInfosResponse {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())
            .pub

        val waitings: MutableList<Waiting> = waitingRepository
            .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                pub, mutableListOf(Status.WAITING, Status.AVAILABLE)
            )

        return WaitingInfosResponse(
            waitings.size,
            waitings.stream()
                .map<WaitingInfo> { waiting: Waiting -> Waiting.toWaitingInfo(waiting) }
                .toList()
        )
    }
}
