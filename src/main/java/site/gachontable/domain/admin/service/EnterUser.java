package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.domain.admin.domain.Admin;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.domain.admin.exception.SeatingNumAlreadyExistsException;
import site.gachontable.presentation.admin.dto.request.EnterUserRequest;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.pub.exception.PubMismatchException;
import site.gachontable.domain.seating.domain.Seating;
import site.gachontable.domain.seating.port.out.SeatingRepository;
import site.gachontable.domain.waiting.domain.Waiting;
import site.gachontable.domain.waiting.port.out.WaitingRepository;
import site.gachontable.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.infra.redis.RedissonLock;
import site.gachontable.independent.type.SuccessCode;

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
        checkSeatingExists(pub, seatingNum);

        Seating seating = Seating.create(
                seatingNum,
                waiting.getTableType(),
                LocalDateTime.now().plusMinutes(pub.getMinutes()),
                pub,
                waiting,
                waiting.getUser());

        seatingRepository.save(seating);
    }

    private void checkSeatingExists(Pub pub, Integer seatingNum) {
        boolean seatingExists = seatingRepository
                .existsByPubAndSeatingNumAndExitTimeAfter(pub, seatingNum, LocalDateTime.now());

        if (seatingExists) {
            throw new SeatingNumAlreadyExistsException();
        }
    }
}
