package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.user.Exception.EmptyQueingCountException;
import site.gachontable.gachontablebe.domain.user.Exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.waiting.Exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Entered {

    private final UserRepository userRepository;
    private final WaitingRepository waitingRepository;

    public String entered(UUID Id) {
        User user = userRepository.findById(Id).orElseThrow(UserNotFoundException::new);
        Waiting waiting = waitingRepository.findByUser(user).orElseThrow(WaitingNotFoundException::new);

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
