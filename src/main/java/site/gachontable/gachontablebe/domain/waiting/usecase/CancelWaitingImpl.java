package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {
    private final WaitingRepository waitingRepository;
    private final PubRepository pubRepository;

    @Override
    public WaitingResponse execute(CancelRequest request) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(PubNotFoundException::new);

        waiting.cancel();
        waitingRepository.save(waiting);

        decreaseWaitingCountFromPub(waiting.getPub());
        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }

    private void decreaseWaitingCountFromPub(Pub pub) {
        pub.decreaseWaitingCount();
        pubRepository.save(pub);
    }
}
