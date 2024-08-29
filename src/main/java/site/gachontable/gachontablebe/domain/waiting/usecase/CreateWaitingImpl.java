package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
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
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateWaitingImpl implements CreateWaiting {
    private static final Integer WAITING_MAX_COUNT = 3;

    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final sendBiztalk sendBiztalk;

     @Value("${biztalk.templateId.waiting}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(AuthDetails authDetails, RemoteWaitingRequest request, String lockKey) { // 원격 웨이팅
        User user = userRepository.findById(authDetails.getUuid())
                .orElseThrow(UserNotFoundException::new);
        Pub pub = pubRepository.findByPubId(request.pubId())
                .orElseThrow(PubNotFoundException::new);

        checkPreConditions(pub, user.getUserTel());

        Waiting waiting = waitingRepository.save(
                Waiting.create(Position.REMOTE, request.headCount(), Status.WAITING, user.getUserTel(), user, pub));

        pub.increaseWaitingCount();

        // TODO : 카카오 알림톡 전송
        sendBiztalk.execute(TEMPLATE_CODE, user.getUserTel(), createVariables(user.getUsername(), pub, waiting, request.headCount()));

        return new WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.getMessage());
    }

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(AuthDetails authDetails, OnsiteWaitingRequest request, String lockKey) { // 현장 웨이팅
        Pub pub = adminRepository.findByUsername(authDetails.getUsername()).orElseThrow(AdminNotFoundException::new)
                .getPub();

        checkPreConditions(pub, request.tel());

        Waiting waiting = waitingRepository.save(
                Waiting.create(Position.ONSITE, request.headCount(), Status.WAITING, request.tel(), null, pub));

        pub.increaseWaitingCount();

        // TODO : 카카오 알림톡 전송
        sendBiztalk.execute(TEMPLATE_CODE, request.tel(), createVariables(request.tel().substring(7), pub, waiting, request.headCount()));

        return new WaitingResponse(true, SuccessCode.ONSITE_WAITING_SUCCESS.getMessage());
    }

    private void checkPreConditions(Pub pub, String tel) throws
            PubNotOpenException, UserWaitingLimitExcessException, WaitingAlreadyExistsException {
        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }

        // 해당 주점에 웨이팅 대기중이면 예외 처리
        if (waitingRepository.findByTelAndPubAndWaitingStatusOrWaitingStatus(
                tel, pub, Status.WAITING, Status.AVAILABLE).isPresent()) {
            throw new WaitingAlreadyExistsException();
        }

        // 같은 번호로 3개 이상의 웨이팅이 대기중이면 예외 처리
        if (waitingRepository.findAllByTelAndWaitingStatusOrWaitingStatus(
                tel, Status.WAITING, Status.AVAILABLE).size() >= WAITING_MAX_COUNT) {
            throw new UserWaitingLimitExcessException();
        }
    }

    public HashMap<String, String> createVariables(String username, Pub pub, Waiting waiting, Integer headCount) {

        List<Waiting> waitings = waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE);
        String order = String.valueOf(waitings.size());
        String callNumber = waiting.getTel().substring(7);

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{username}", username);
        variables.put("#{pub}", pub.getPubName());
        variables.put("#{headCount}", String.valueOf(headCount));
        variables.put("#{order}", order);
        variables.put("#{callNumber}", callNumber);
        variables.put("#{waitingId}", String.valueOf(waiting.getWaitingId()));

        return variables;
    }
}
