package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ReadyUser {
    private final WaitingRepository waitingRepository;
    private final sendBiztalk sendBiztalk;

    @Value("${biztalk.templateId.ready}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    public void execute(Pub pub, String lockKey) {

        // TODO : 카카오 알림톡 전송
        Waiting waiting = waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE).get(2);

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{pub}", pub.getPubName());
        variables.put("#{username}", waiting.getUser().getUsername());
        variables.put("#{CallNumber}", waiting.getTel().substring(7));
        variables.put("#{waitingId}", waiting.getWaitingId().toString());

        sendBiztalk.execute(TEMPLATE_CODE, waiting.getTel(), variables);
    }
}
