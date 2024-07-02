package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.OnsiteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Component
@RequiredArgsConstructor
public class CreateWaitingImpl implements CreateWaiting {
    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;

    @Override
    public WaitingResponse execute(User user, RemoteWaitingRequest request) { // 원격 웨이팅
        Pub pub = pubRepository.findByPubId(request.pubId()).orElseThrow(PubNotFoundException::new);

        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }
        waitingRepository.save(
                Waiting.create(Position.REMOTE, request.headCount(), Status.WAITING, user, pub));
        pub.updateWaitingCount(pub.getWaitingCount() + 1);

        // TODO : 카카오 알림톡 전송


        return new WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.getMessage());
    }

    @Override
    public WaitingResponse execute(OnsiteWaitingRequest request) { // 현장 웨이팅
        Pub pub = pubRepository.findByPubId(request.pubId()).orElseThrow(PubNotFoundException::new);

        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }
        Waiting waiting = Waiting.create(Position.ONSITE, request.headCount(), Status.WAITING, null, pub);
        // TODO : 현장 웨이팅 로직 가입 여부 논의 필요
        waitingRepository.save(waiting);

        // TODO : 카카오 알림톡 전송

        // TODO : 웨이팅 로직 변경 적용

        return new WaitingResponse(true, SuccessCode.ONSITE_WAITING_SUCCESS.getMessage());
    }


}
