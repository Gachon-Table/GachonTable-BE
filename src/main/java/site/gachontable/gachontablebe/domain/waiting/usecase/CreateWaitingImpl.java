package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.SeatingAlreadyExistsException;
import site.gachontable.gachontablebe.domain.waiting.exception.UserWaitingLimitExcessException;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingAlreadyExistsException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateWaitingImpl implements CreateWaiting {
    private static final Integer WAITING_MAX_COUNT = 3;

    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;
    private final SeatingRepository seatingRepository;
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

        checkPreConditions(pub, user);

        Waiting waiting = waitingRepository.save(
                Waiting.create(Position.REMOTE, request.headCount(), Status.WAITING, user.getUserTel(), user, pub));

        pub.increaseWaitingCount();

        sendBiztalk.execute(TEMPLATE_CODE, user.getUserTel(), createVariables(user.getUsername(), pub, waiting, request.headCount()));

        return new WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.getMessage());
    }

/*    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(AuthDetails authDetails, OnsiteWaitingRequest request, String lockKey) { // 현장 웨이팅
        Pub pub = adminRepository.findByUsername(authDetails.getUsername()).orElseThrow(AdminNotFoundException::new)
                .getPub();

        checkPreConditions(pub, request.tel());

        Waiting waiting = waitingRepository.save(
                Waiting.create(Position.ONSITE, request.headCount(), Status.WAITING, request.tel(), null, pub));

        pub.increaseWaitingCount();

        sendBiztalk.execute(TEMPLATE_CODE, request.tel(), createVariables(request.tel().substring(9), pub, waiting, request.headCount()));

        return new WaitingResponse(true, SuccessCode.ONSITE_WAITING_SUCCESS.getMessage());
    }*/

    private void checkPreConditions(Pub pub, User user) throws
            PubNotOpenException, UserWaitingLimitExcessException, WaitingAlreadyExistsException {
        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }

        // 현재 주점을 이용중이라면 예외 처리
        checkSeatings(user);

        // 해당 주점에 웨이팅 대기중이면 예외 처리
        checkDuplicatePubWaiting(pub, user);

        // 같은 번호로 3개 이상의 웨이팅이 대기중이면 예외 처리
        checkWaitingLimit(user);
    }

    private void checkSeatings(User user) {
        seatingRepository.findFirstByUserAndExitTimeAfter(user, LocalDateTime.now())
                .ifPresent(seating -> {
                    throw new SeatingAlreadyExistsException();
                });
    }

    private void checkWaitingLimit(User user) {
        if (waitingRepository.findAllByTelAndWaitingStatusOrWaitingStatus(
                user.getUserTel(), Status.WAITING, Status.AVAILABLE).size() >= WAITING_MAX_COUNT) {
            throw new UserWaitingLimitExcessException();
        }
    }

    private void checkDuplicatePubWaiting(Pub pub, User user) {
        waitingRepository
                .findByTelAndPubAndWaitingStatusOrWaitingStatus(
                        user.getUserTel(), pub, Status.WAITING, Status.AVAILABLE)
                .ifPresent(waiting -> {
                    throw new WaitingAlreadyExistsException();
                });
    }

    private HashMap<String, String> createVariables(String username, Pub pub, Waiting waiting, Integer headCount) {

        List<Waiting> waitings = waitingRepository
                .findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE);
        String order = String.valueOf(waitings.size());
        String callNumber = waiting.getTel().substring(9);

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
