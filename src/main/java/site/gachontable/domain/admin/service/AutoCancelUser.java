package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.gachontable.domain.shared.event.SentBiztalkEvent;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.domain.waiting.type.Status;
import site.gachontable.infra.redis.RedissonLock;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoCancelUser {

    private final WaitingRepository waitingRepository;
    private final ReadyUser readyUser;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${biztalk.templateId.forceCancel}")
    private String FORCE_CANCEL_TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    public void execute(UUID waitingId, HashMap<String, String> variables, String lockKey) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(WaitingNotFoundException::new);

        if (waiting.getWaitingStatus().equals(Status.AVAILABLE)) {
            waiting.cancel();
            waiting.getPub().decreaseWaitingCount();

            eventPublisher.publishEvent(
                    SentBiztalkEvent.of(FORCE_CANCEL_TEMPLATE_CODE, waiting.getTel(), variables));

            readyUser.execute(waiting.getPub());
        }
    }
}
