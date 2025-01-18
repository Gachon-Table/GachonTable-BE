package site.gachontable.domain.waiting.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.gachontable.domain.admin.service.ReadyUser;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.shared.event.SentBiztalkEvent;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.in.CancelWaiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.presentation.waiting.dto.request.CancelRequest;
import site.gachontable.presentation.waiting.dto.response.WaitingResponse;
import site.gachontable.domain.waiting.type.Status;
import site.gachontable.infra.redis.RedissonLock;
import site.gachontable.independent.type.SuccessCode;

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
