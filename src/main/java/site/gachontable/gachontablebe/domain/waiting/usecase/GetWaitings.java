package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.WaitingInfosResponse;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

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
