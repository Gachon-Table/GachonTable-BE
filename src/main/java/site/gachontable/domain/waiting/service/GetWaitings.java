package site.gachontable.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.presentation.admin.dto.response.WaitingInfosResponse;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.type.Status;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWaitings {

    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public WaitingInfosResponse execute(AuthDetails authDetails) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        List<Waiting> waitings = waitingRepository
                .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                        pub, Arrays.asList(Status.WAITING, Status.AVAILABLE));

        return new WaitingInfosResponse(
                waitings.size(),
                waitings.stream()
                        .map(Waiting::toWaitingInfo)
                        .toList()
                );
    }
}
