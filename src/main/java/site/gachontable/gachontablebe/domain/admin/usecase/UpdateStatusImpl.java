package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.UpdateStatusRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class UpdateStatusImpl implements UpdateStatus{
    private final AdminRepository adminRepository;
    private final WaitingRepository waitingRepository;

    @Transactional
    @Override
    public RegisterResponse execute(AuthDetails authDetails, UpdateStatusRequest request) {
        Pub pub = adminRepository.findByUsername(authDetails.getUsername())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        pub.updateOpenStatus(request.openStatus());

        waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE)
                .forEach(Waiting::cancel);

        return new RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.getMessage());
    }
}
