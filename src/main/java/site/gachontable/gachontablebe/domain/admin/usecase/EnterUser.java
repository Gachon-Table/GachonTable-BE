package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.EnterUserResponse;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.EmptyWaitingCountException;
import site.gachontable.gachontablebe.domain.pub.exception.PubMismatchException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class EnterUser {

    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;

    public EnterUserResponse execute(Admin admin, Long waitingId) {

        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(WaitingNotFoundException::new);
        Pub pub = waiting.getPub();

        if (!pub.equals(admin.getPub())) {
            throw new PubMismatchException();
        }

        setWaitingEntered(waiting);
        decreaseWaitingCount(pub);

        return new EnterUserResponse(SuccessCode.ENTERED_SUCCESS.getMessage());
    }

    private void decreaseWaitingCount(Pub Pub) {
        if (Pub.getWaitingCount() == 0) {
            throw new EmptyWaitingCountException();
        }

        Pub.decreaseWaitingCount();
        pubRepository.save(Pub);
    }

    private void setWaitingEntered(Waiting givenWaiting) {
        givenWaiting.enter();
        waitingRepository.save(givenWaiting);
    }
}
