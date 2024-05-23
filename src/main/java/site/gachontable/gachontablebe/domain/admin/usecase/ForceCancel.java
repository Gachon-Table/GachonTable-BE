package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForceCancel {

    private final UserRepository userRepository;
    private final WaitingRepository waitingRepository;

    @Transactional
    public void cancel(UUID Id) {
        Optional<User> user = userRepository.findById(Id);
        Waiting waiting = waitingRepository.findByUser(user.orElse(null));

        if (user.isEmpty()) {
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }

        if (waiting == null) {
            throw new ServiceException(ErrorCode.WAITING_NOT_FOUND);
        }

        if (user.get().getQueueingCount() == 0) {
            throw new ServiceException(ErrorCode.INVALID_QUEUEING_COUNT);
        }

        decreaseQueueingCount(user.orElse(null));
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
