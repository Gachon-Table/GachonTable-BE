package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
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
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.biztalk.sendBiztalk;
import site.gachontable.gachontablebe.global.biztalk.dto.request.CallUserTemplateParameterRequest;
import site.gachontable.gachontablebe.global.biztalk.dto.request.TemplateParameter;
import site.gachontable.gachontablebe.global.biztalk.dto.request.CancelWaitingTemplateParameterRequest;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CallUser {
    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final TransactionTemplate transactionTemplate;
    private final sendBiztalk sendBiztalk;

    private static final String CALL_TEMPLATE_CODE = "CALL";
    private static final String FORCE_CANCEL_TEMPLATE_CODE = "FCANCEL";

    @RedissonLock(key = "#lockKey")
    public String execute(AuthDetails authDetails, CallUserRequest request, String lockKey) {
        Admin admin = adminRepository.findById(authDetails.getUuid()).
                orElseThrow(AdminNotFoundException::new);
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }

        waiting.toAvailable();

        // TODO : 카카오 알림톡 전송
        TemplateParameter templateParameter = new CallUserTemplateParameterRequest(pub.getPubName(), waiting.getWaitingId().toString());
        sendBiztalk.execute(CALL_TEMPLATE_CODE, waiting.getTel(), templateParameter);

        // 사용자가 5분 안에 응답하는지 확인하기 위해 작업을 예약합니다.
        transactionTemplate.execute(status -> {
            executorService.schedule(() -> {
                transactionTemplate.execute(innerStatus -> {
                    if (waiting.getWaitingStatus().equals(Status.AVAILABLE)) {
                        // 사용자가 5분 이내에 응답하지 않으면 예약을 취소합니다.
                        waiting.cancel();
                        pub.decreaseWaitingCount();

                        // TODO : 카카오 알림톡 전송
                        sendBiztalk.execute(FORCE_CANCEL_TEMPLATE_CODE, waiting.getTel(), new CancelWaitingTemplateParameterRequest(pub.getPubName()));
                    }
                    return null;
                });
            }, 5, TimeUnit.MINUTES);
            return null;
        });

        return SuccessCode.USER_CALL_SUCCESS.getMessage();
    }
}
