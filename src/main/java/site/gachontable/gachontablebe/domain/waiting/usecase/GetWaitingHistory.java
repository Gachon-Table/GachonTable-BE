package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;
import site.gachontable.gachontablebe.domain.seating.exception.SeatingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingHistoryResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWaitingHistory {

    private final WaitingRepository waitingRepository;
    private final SeatingRepository seatingRepository;

    @Transactional(readOnly = true)
    public List<WaitingHistoryResponse> execute(AuthDetails authDetails) {
        String tel = authDetails.getTel();

        List<Waiting> waitings = waitingRepository.findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(
                tel, Arrays.asList(Status.ENTERED, Status.CANCELED));

        return waitings.stream()
                .map(waiting -> {
                    if (waiting.getWaitingStatus() == Status.ENTERED) {
                        LocalDateTime exitTime = seatingRepository.findExitTimeByWaiting(waiting)
                                .orElseThrow(SeatingNotFoundException::new);
                        return WaitingHistoryResponse.of(waiting, exitTime);
                    }
                    return WaitingHistoryResponse.of(waiting, null);
                })
                .toList();
    }
}
