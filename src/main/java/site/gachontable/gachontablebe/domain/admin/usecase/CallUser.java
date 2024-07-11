package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.CallUserRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubMismatchException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CallUser {
    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;
    private final PubRepository pubRepository;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public String execute(AuthDetails authDetails, CallUserRequest request) {
        Admin admin = adminRepository.findById(authDetails.getUuid()).
                orElseThrow(AdminNotFoundException::new);
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }

        updateWaitingStatusToAvailable(waiting);
        // TODO : 카카오 알림톡 전송

        // 사용자가 5분 안에 응답하는지 확인하기 위해 작업을 예약합니다.
        executorService.schedule(() -> {
            if (getUpdatedhWaitingStatus(request).equals(Status.AVAILABLE)) {
                // 사용자가 5분 이내에 응답하지 않으면 예약을 취소합니다.
                updateWaitingStatusToCanceled(waiting);
                decreaseWaitingCount(pub);
                // TODO : 카카오 알림톡 전송
            }
        }, 5, TimeUnit.MINUTES);

        return SuccessCode.USER_CALL_SUCCESS.getMessage();
    }

    private void updateWaitingStatusToAvailable(Waiting waiting) {
        waiting.toAvailable();
        waitingRepository.save(waiting);
    }

    private void updateWaitingStatusToCanceled(Waiting waiting) {
        waiting.cancel();
        waitingRepository.save(waiting);
    }

    private void decreaseWaitingCount(Pub Pub) {
        Pub.decreaseWaitingCount();
        pubRepository.save(Pub);
    }

    private Status getUpdatedhWaitingStatus(CallUserRequest request) {
        return waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new).getWaitingStatus();
    }
}
