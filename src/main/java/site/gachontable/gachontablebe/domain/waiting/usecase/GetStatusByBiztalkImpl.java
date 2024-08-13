package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetStatusByBiztalkImpl implements GetStatusByBiztalk {
    private final WaitingRepository waitingRepository;

    @Override
    public StatusResponse execute(UUID waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        return StatusResponse.of(
                waiting.getWaitingId(),
                pub.getPubName(),
                waiting.getWaitingStatus().getStatusKo(),
                getIndexOfWaiting(waiting, pub),
                String.valueOf(waiting.getCreatedAt()),
                waiting.getHeadCount()
        );
    }

    private Integer getIndexOfWaiting(Waiting waiting, Pub pub) {
        List<Waiting> waitings = waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE);

        return waitings.indexOf(waiting) + 1;
    }
}
