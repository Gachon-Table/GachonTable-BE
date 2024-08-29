package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {
    private final WaitingRepository waitingRepository;
    private final sendBiztalk sendBiztalk;

    @Value("${biztalk.templateId.cancel}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(CancelRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);

        waiting.cancel();
        waiting.getPub().decreaseWaitingCount();

        // TODO : 카카오 알림톡 전송
        Pub pub = waiting.getPub();
        sendBiztalk.execute(TEMPLATE_CODE, waiting.getTel(), (HashMap<String, String>) Map.of("#{pub}", pub.getPubName()));

        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }
}
