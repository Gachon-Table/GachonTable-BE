package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.user.Exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.waiting.Exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForceCancel {

    private final UserRepository userRepository;
    private final WaitingRepository waitingRepository;

    @Transactional
    public void cancel(UUID Id) {
        User user = userRepository.findById(Id).orElseThrow(UserNotFoundException::new);
        Waiting waiting = waitingRepository.findByUser(user).orElseThrow(WaitingNotFoundException::new);

        decreaseQueueingCount(user);
        setWaitingCancel(waiting);
    }

    private void decreaseQueueingCount(User user) {
        User updatedUser = user.decreaseQueueingCount();
        userRepository.save(updatedUser);
    }

    private void setWaitingCancel(Waiting waiting) {
        Waiting updatedWaiting = waiting.cancel();
        waitingRepository.save(updatedWaiting);
    }
}
