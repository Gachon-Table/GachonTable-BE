package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.dto.EnteredRequest;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.user.Exception.EmptyQueingCountException;
import site.gachontable.gachontablebe.domain.user.Exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.waiting.Exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class Entered {

    private final UserRepository userRepository;
    private final WaitingRepository waitingRepository;
    private final PubRepository pubRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;

    public String entered(String authorizationHeader, EnteredRequest enteredRequest) {
        String token = authorizationHeader.substring(7);
        Authentication authentication = jwtProvider.getAuthentication(token);
        Admin admin = adminRepository.findByAdminName(authentication.getName()).orElseThrow(AdminNotFoundException::new);
        Pub pub = pubRepository.findByPubId(admin.getPub().getPubId()).orElseThrow(PubNotFoundException::new);
        User user = userRepository.findById(enteredRequest.userId()).orElseThrow(UserNotFoundException::new);
        Waiting waiting = waitingRepository.findByUserAndPub(user, pub).orElseThrow(WaitingNotFoundException::new);

        decreaseQueueingCount(user);
        setWaitingEntered(waiting);

        return SuccessCode.ENTERED_SUCCESS.getMessage();
    }

    private void decreaseQueueingCount(User givenUser) {
        if (givenUser.getQueueingCount() == 0) {
            throw new EmptyQueingCountException();
        }

        givenUser.decreaseQueueingCount();
        userRepository.save(givenUser);
    }

    private void setWaitingEntered(Waiting givenWaiting) {
        givenWaiting.entered();
        waitingRepository.save(givenWaiting);
    }
}
