package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.CallUserRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.exception.PubMismatchException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.global.biztalk.SendBiztalk;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CallUser {

    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;
    private final SendBiztalk sendBiztalk;
    private final AutoCancelUser autoCancelUser;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);

    @Value("${biztalk.templateId.call}")
    private String CALL_TEMPLATE_CODE;

    @RedissonLock(key = "#lockKey")
    public String execute(AuthDetails authDetails, CallUserRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId())
                .orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        checkPubMatches(authDetails, pub);

        waiting.toAvailable();

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{pub}", pub.getPubName());
        sendBiztalk.execute(CALL_TEMPLATE_CODE, waiting.getTel(), variables);

        scheduleAutoCancel(request.waitingId(), variables);

        return SuccessCode.USER_CALL_SUCCESS.getMessage();
    }


    private void scheduleAutoCancel(UUID waitingId, HashMap<String, String> variables) {
        executorService.schedule(() ->
                autoCancelUser.execute(waitingId, variables, "자동 취소"), 5, TimeUnit.MINUTES);
    }

    private void checkPubMatches(AuthDetails authDetails, Pub pub) {
        Admin admin = adminRepository.findById(authDetails.getUuid()).
                orElseThrow(AdminNotFoundException::new);

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }
    }
}
