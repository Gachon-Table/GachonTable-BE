package site.gachontable.domain.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.`in`.UpdateStatus
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.port.out.WaitingRepository
import site.gachontable.domain.waiting.type.Status
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.UpdateStatusRequest
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@Service
class UpdateStatusImpl(
    private val adminRepository: AdminRepository,
    private val waitingRepository: WaitingRepository,
) : UpdateStatus {
    @Transactional
    override fun executeForOpenStatus(authDetails: AuthDetails, request: UpdateStatusRequest): RegisterResponse {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())
            .pub

        pub.updateOpenStatus(request.status)

        waitingRepository.findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
            pub, listOf<Status>(Status.WAITING, Status.AVAILABLE)
        )
            .forEach(Waiting::cancel)

        return RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.message)
    }

    @Transactional
    override fun executeForWaitingStatus(authDetails: AuthDetails, request: UpdateStatusRequest): RegisterResponse {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())
            .pub

        pub.updateWaitingStatus(request.status)

        return RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.message)
    }
}
