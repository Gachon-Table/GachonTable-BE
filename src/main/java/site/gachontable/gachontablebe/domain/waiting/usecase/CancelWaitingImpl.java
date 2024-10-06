package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.usecase.ReadyUser;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.biztalk.SendBiztalk;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {

    private final WaitingRepository waitingRepository;
    private final ReadyUser readyUser;
    private final SendBiztalk sendBiztalk;

    @Value("${biztalk.templateId.cancel}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(CancelRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);

        Pub pub = waiting.getPub();
        List<Waiting> limitedWaitings = getLimitedWaitings(pub);

        waiting.cancel();
        waiting.getPub().decreaseWaitingCount();

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{pub}", pub.getPubName());
        sendBiztalk.execute(TEMPLATE_CODE, waiting.getTel(), variables);

        if (isWaitingContains(waiting, limitedWaitings)) {
            readyUser.execute(pub);
        }

        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }

    private List<Waiting> getLimitedWaitings(Pub pub) {
        return waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE)
                .stream()
                .limit(3)
                .toList();
    }

    private boolean isWaitingContains(Waiting waiting, List<Waiting> limitedWaitings) {
        return limitedWaitings.contains(waiting);
    }
}
