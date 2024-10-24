package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStatusImpl implements GetStatus {

    private final WaitingRepository waitingRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StatusResponse> execute(AuthDetails authDetails) {
        String tel = authDetails.getTel();

        List<Waiting> waitings = waitingRepository
                .findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(
                        tel, Arrays.asList(Status.WAITING, Status.AVAILABLE));

        return waitings.stream()
                .map(waiting ->
                        getStatusResponse(waiting, getWaitingsInPubFrom(waiting)))
                .toList();
    }

    private List<Waiting> getWaitingsInPubFrom(Waiting waiting) {
        return waitingRepository
                .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                        waiting.getPub(), Arrays.asList(Status.WAITING, Status.AVAILABLE));
    }

    private StatusResponse getStatusResponse(Waiting waiting, List<Waiting> waitings) {
        return StatusResponse.of(waiting, waiting.getPub(), waitings.indexOf(waiting) + 1);
    }
}
