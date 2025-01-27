package site.gachontable.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.in.GetStatusByBiztalk;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.presentation.waiting.dto.response.StatusResponse;
import site.gachontable.domain.waiting.type.Status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStatusByBiztalkImpl implements GetStatusByBiztalk {

    private final WaitingRepository waitingRepository;

    @Transactional(readOnly = true)
    @Override
    public StatusResponse execute(UUID waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        return StatusResponse.of(waiting, pub, getIndexOfWaiting(waiting, pub));
    }

    private Integer getIndexOfWaiting(Waiting waiting, Pub pub) {
        if (waiting.getWaitingStatus() == Status.CANCELED) {
            return -1;
        }
        if (waiting.getWaitingStatus() == Status.ENTERED) {
            return -2;
        }

        List<Waiting> waitings = waitingRepository
                .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                        pub, Arrays.asList(Status.WAITING, Status.AVAILABLE));

        return waitings.indexOf(waiting) + 1;
    }
}
