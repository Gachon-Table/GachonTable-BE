package site.gachontable.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.gachontable.domain.waiting.port.in.CreateWaiting;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.pub.domain.repository.PubRepository;
import site.gachontable.domain.pub.exception.PubNotFoundException;
import site.gachontable.infra.biztalk.event.SentBiztalkEvent;
import site.gachontable.domain.member.domain.User;
import site.gachontable.domain.member.port.out.UserRepository;
import site.gachontable.domain.member.exception.UserNotFoundException;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.exception.UserWaitingLimitExcessException;
import site.gachontable.domain.waiting.exception.WaitingAlreadyExistsException;
import site.gachontable.presentation.waiting.dto.request.RemoteWaitingRequest;
import site.gachontable.presentation.waiting.dto.response.WaitingResponse;
import site.gachontable.domain.waiting.type.Position;
import site.gachontable.domain.waiting.type.Status;
import site.gachontable.infra.redis.RedissonLock;
import site.gachontable.independent.type.SuccessCode;

import java.util.Arrays;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CreateWaitingImpl implements CreateWaiting {

    private static final Integer WAITING_MAX_COUNT = 3;

    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${biztalk.templateId.waiting}")
    private String TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    @Override
    public WaitingResponse execute(AuthDetails authDetails, RemoteWaitingRequest request, String lockKey) { // 원격 웨이팅
        User user = userRepository.findById(authDetails.getUuid())
                .orElseThrow(UserNotFoundException::new);
        Pub pub = pubRepository.findById(request.pubId())
                .orElseThrow(PubNotFoundException::new);

        checkPreConditions(pub, user);

        Waiting waiting = waitingRepository.save(
                Waiting.create(Position.REMOTE, request.tableType(), Status.WAITING, user.getUserTel(), user, pub));

        pub.increaseWaitingCount();

        eventPublisher.publishEvent(
                SentBiztalkEvent.of(
                        TEMPLATE_CODE,
                        user.getUserTel(),
                        createVariables(user.getUsername(), pub, waiting, request.tableType().getNameKo())
                ));

        return new WaitingResponse(true, SuccessCode.REMOTE_WAITING_SUCCESS.getMessage());
    }

    private void checkPreConditions(Pub pub, User user) {
        // 신청할 주점의 상태 확인
        pub.checkStatus();

        // 해당 주점에 웨이팅 대기중이면 예외 처리
        checkDuplicatePubWaiting(pub, user);

        // 같은 번호로 3개 이상의 웨이팅이 대기중이면 예외 처리
        checkWaitingLimit(user);
    }

    private void checkWaitingLimit(User user) {
        if (waitingRepository.countByTelAndWaitingStatuses(
                user.getUserTel(), Status.WAITING, Status.AVAILABLE) >= WAITING_MAX_COUNT) {
            throw new UserWaitingLimitExcessException();
        }
    }

    private void checkDuplicatePubWaiting(Pub pub, User user) {
        boolean duplicatePubWaitingExists = waitingRepository
                .existsByTelAndPubAndWaitingStatusIn(
                        user.getUserTel(), pub, Arrays.asList(Status.WAITING, Status.AVAILABLE));

        if (duplicatePubWaitingExists) {
            throw new WaitingAlreadyExistsException();
        }
    }

    private HashMap<String, String> createVariables(String username, Pub pub, Waiting waiting, String tableType) {
        String order = String.valueOf(pub.getWaitingCount());

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{username}", username);
        variables.put("#{pub}", pub.getPubName());
        variables.put("#{headCount}", tableType);
        variables.put("#{order}", order);
        variables.put("#{waitingId}", String.valueOf(waiting.getWaitingId()));

        return variables;
    }
}
