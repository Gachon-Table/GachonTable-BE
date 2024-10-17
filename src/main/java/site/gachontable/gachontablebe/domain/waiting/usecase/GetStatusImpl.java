package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetStatusImpl implements GetStatus {

    private final WaitingRepository waitingRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StatusResponse> execute(AuthDetails authDetails) {
        String tel = authDetails.getTel();

        return getPubsFromWaitings(tel).stream()
                .flatMap(pub -> {
                    List<Waiting> waitings = waitingRepository
                            .findAllByPubAndTelAndWaitingStatusInOrderByCreatedAtAsc(
                                    pub, tel, Arrays.asList(Status.WAITING, Status.AVAILABLE));

                    return getStatusResponse(pub, waitings);
                })
                .toList();
    }

    private Stream<StatusResponse> getStatusResponse(Pub pub, List<Waiting> waitings) {
        return waitings.stream()
                .map(waiting -> StatusResponse.of(waiting, pub, waitings.indexOf(waiting) + 1));
    }

    private List<Pub> getPubsFromWaitings(String tel) {
        return waitingRepository.findDistinctPubsByTel(tel);
    }
}
