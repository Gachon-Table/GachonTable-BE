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
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnterUser {
    private final WaitingRepository waitingRepository;
    private final AdminRepository adminRepository;
    private final ReadyUser readyUser;
    private final SeatingRepository seatingRepository;

    @RedissonLock(key = "#lockKey")
    public String execute(AuthDetails authDetails, EnterUserRequest request, String lockKey) {
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        checkPubMatches(authDetails, pub);

        waiting.enter();
        pub.decreaseWaitingCount();
        createSeating(pub, waiting, request.seatingNum());

        readyUser.execute(pub);

        return SuccessCode.ENTERED_SUCCESS.getMessage();
    }

    private void checkPubMatches(AuthDetails authDetails, Pub pub) {
        Admin admin = adminRepository.findById(authDetails.getUuid()).
                orElseThrow(AdminNotFoundException::new);

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }
    }

    private void createSeating(Pub pub, Waiting waiting, Integer seatingNum) {
        Seating seating = Seating.create(
                seatingNum,
                LocalDateTime.now().plusHours(pub.getHours()),
                pub,
                waiting,
                waiting.getUser());

        seatingRepository.save(seating);
    }
}
