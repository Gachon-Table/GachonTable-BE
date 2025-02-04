package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.domain.admin.port.in.UpdateStatus;
import site.gachontable.presentation.admin.dto.request.UpdateStatusRequest;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.presentation.shared.dto.response.RegisterResponse;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.type.Status;
import site.gachontable.independent.type.SuccessCode;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UpdateStatusImpl implements UpdateStatus {

    private final AdminRepository adminRepository;
    private final WaitingRepository waitingRepository;

    @Transactional
    @Override
    public RegisterResponse executeForOpenStatus(AuthDetails authDetails, UpdateStatusRequest request) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        pub.updateOpenStatus(request.status());

        waitingRepository
                .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                        pub, Arrays.asList(Status.WAITING, Status.AVAILABLE))
                .forEach(Waiting::cancel);

        return new RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.getMessage());
    }

    @Transactional
    @Override
    public RegisterResponse executeForWaitingStatus(AuthDetails authDetails, UpdateStatusRequest request) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        pub.updateWaitingStatus(request.status());

        return new RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.getMessage());
    }
}
