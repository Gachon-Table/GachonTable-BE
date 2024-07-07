package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.PubWaitingListResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWaitings {
    private final WaitingRepository waitingRepository;
    private final PubRepository pubRepository;

    public PubWaitingListResponse excute(Pub pub) {
        List<Waiting> waitings = waitingRepository.findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE);
        return new PubWaitingListResponse(
                waitings.size(),
                waitings.stream()
                        .map(Waiting::toWaitingInfo)
                        .toList()
                );
    }
}