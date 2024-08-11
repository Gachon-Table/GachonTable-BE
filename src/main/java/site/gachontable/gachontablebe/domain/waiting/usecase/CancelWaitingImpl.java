package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.biztalk.dto.request.TemplateParameter;
import site.gachontable.gachontablebe.global.biztalk.dto.request.CancelWaitingTemplateParameterRequest;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {
    private static final String TEMPLATE_CODE = "CANCEL";
  
    private final WaitingRepository waitingRepository;
    private final sendBiztalk sendBiztalk;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(CancelRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);

        waiting.cancel();
        waiting.getPub().decreaseWaitingCount();
      
        // TODO : 카카오 알림톡 전송
        Pub pub = waiting.getPub();
        TemplateParameter templateParameter = new CancelWaitingTemplateParameterRequest(pub.getPubName());
        sendBiztalk.execute(TEMPLATE_CODE, waiting.getTel(), templateParameter);
      
        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }
}
