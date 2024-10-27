package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.usecase.ReadyUser;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.shared.event.SentBiztalkEvent;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {

    private final WaitingRepository waitingRepository;
    private final ReadyUser readyUser;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${biztalk.templateId.cancel}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(CancelRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId())
                .orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        List<Waiting> top3Waitings = getTop3Waitings(pub);

        waiting.cancel();
        waiting.getPub().decreaseWaitingCount();

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{pub}", pub.getPubName());
        eventPublisher.publishEvent(
                SentBiztalkEvent.of(TEMPLATE_CODE, waiting.getTel(), variables));

        if (isWaitingIn(waiting, top3Waitings)) {
            readyUser.execute(pub);
        }

        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }

    private List<Waiting> getTop3Waitings(Pub pub) {
        return waitingRepository
                .findTop3ByPubAndWaitingStatusInOrderByCreatedAtAsc(
                        pub, Arrays.asList(Status.WAITING, Status.AVAILABLE));
    }

    private boolean isWaitingIn(Waiting waiting, List<Waiting> limitedWaitings) {
        return limitedWaitings.contains(waiting);
    }
}
