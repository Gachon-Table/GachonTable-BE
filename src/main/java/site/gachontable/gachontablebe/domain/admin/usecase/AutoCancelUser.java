package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.biztalk.SendBiztalk;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoCancelUser {

    private final WaitingRepository waitingRepository;
    private final SendBiztalk sendBiztalk;
    private final ReadyUser readyUser;

    @Value("${biztalk.templateId.forceCancel}")
    private String FORCE_CANCEL_TEMPLATE_CODE;

    // TODO: LockKey 수정 필요
    @RedissonLock(key = "#waitingId")
    public void execute(UUID waitingId, Pub pub, HashMap<String, String> variables) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(WaitingNotFoundException::new);

        if (waiting.getWaitingStatus().equals(Status.AVAILABLE)) {
            waiting.cancel();

            pub.decreaseWaitingCount();

            sendBiztalk.execute(FORCE_CANCEL_TEMPLATE_CODE, waiting.getTel(), variables);

            readyUser.execute(pub);
        }
    }
}