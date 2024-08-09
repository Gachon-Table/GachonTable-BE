package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.biztalk.dto.request.TemplateParameter;
import site.gachontable.gachontablebe.global.biztalk.dto.request.CancelWaitingTemplateParameterRequest;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {
    private final WaitingRepository waitingRepository;
    private final PubRepository pubRepository;
    private final sendBiztalk sendBiztalk;

    private static final String TEMPLATE_CODE = "CANCEL";

    @Override
    public WaitingResponse execute(CancelRequest request) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);

        waiting.cancel();
        waitingRepository.save(waiting);

        decreaseWaitingCountFromPub(waiting.getPub());

        // TODO : 카카오 알림톡 전송
        Pub pub = waiting.getPub();
        TemplateParameter templateParameter = new CancelWaitingTemplateParameterRequest(pub.getPubName());
        sendBiztalk.execute(TEMPLATE_CODE, waiting.getTel(), templateParameter);

        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }

    private void decreaseWaitingCountFromPub(Pub pub) {
        pub.decreaseWaitingCount();
        pubRepository.save(pub);
    }
}
