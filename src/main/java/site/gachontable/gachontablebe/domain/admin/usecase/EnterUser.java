package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.EnterUserRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.exception.PubMismatchException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class EnterUser {
    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;
    private final ReadyUser readyUser;

    @RedissonLock(key = "#lockKey")
    public String execute(AuthDetails authDetails, EnterUserRequest request, String lockKey) {
        Admin admin = adminRepository.findById(authDetails.getUuid()).
                orElseThrow(AdminNotFoundException::new);
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }

        waiting.enter();
        pub.decreaseWaitingCount();

        readyUser.execute(pub);

        return SuccessCode.ENTERED_SUCCESS.getMessage();
    }
}
