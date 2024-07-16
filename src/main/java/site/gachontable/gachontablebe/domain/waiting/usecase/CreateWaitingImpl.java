package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.UserWaitingLimitExcessException;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingAlreadyExistsException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.OnsiteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class CreateWaitingImpl implements CreateWaiting {
    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;
    private static final Integer WAITING_MAX_COUNT = 3;

    @Override
    public WaitingResponse execute(AuthDetails authDetails, RemoteWaitingRequest request) { // 원격 웨이팅
        User user = userRepository.findById(authDetails.getUuid())
                .orElseThrow(UserNotFoundException::new);
        Pub pub = pubRepository.findByPubId(request.pubId())
                .orElseThrow(PubNotFoundException::new);

        checkPreConditions(pub, user.getUserTel());

        waitingRepository.save(
                Waiting.create(Position.REMOTE, request.headCount(), Status.WAITING, user.getUserTel(), user, pub));

        pub.updateWaitingCount(pub.getWaitingCount() + 1);
        pubRepository.save(pub);
        // TODO : 카카오 알림톡 전송

        return new WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.getMessage());
    }

    @Override
    public WaitingResponse execute(OnsiteWaitingRequest request) { // 현장 웨이팅
        Pub pub = pubRepository.findByPubId(request.pubId())
                .orElseThrow(PubNotFoundException::new);

        checkPreConditions(pub, request.tel());

        waitingRepository.save(
                Waiting.create(Position.ONSITE, request.headCount(), Status.WAITING, request.tel(), null, pub));

        pub.updateWaitingCount(pub.getWaitingCount() + 1);
        pubRepository.save(pub);
        // TODO : 카카오 알림톡 전송

        return new WaitingResponse(true, SuccessCode.ONSITE_WAITING_SUCCESS.getMessage());
    }

    private void checkPreConditions(Pub pub, String tel) {
        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }

        // 같은 번호로 3개 이상의 웨이팅이 대기중이면 예외 처리
        if (waitingRepository.findAllByTelAndWaitingStatusOrWaitingStatus(
                tel, Status.WAITING, Status.AVAILABLE).size() >= WAITING_MAX_COUNT) {
            throw new UserWaitingLimitExcessException();
        }

        // 해당 주점에 웨이팅 대기중이면 예외 처리
        if (waitingRepository.findByTelAndPubAndWaitingStatusOrWaitingStatus(
                tel, pub, Status.WAITING, Status.AVAILABLE).isPresent()) {
            throw new WaitingAlreadyExistsException();
        }
    }

}
