package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class CancelWaitingImpl implements CancelWaiting {
    private final UserRepository userRepository;
    private final WaitingRepository waitingRepository;
    private final PubRepository pubRepository;

    @Override
    public WaitingResponse execute(AuthDetails authDetails, CancelRequest request) {
        User user = userRepository.findById(authDetails.getUuid()).
                orElseThrow(UserNotFoundException::new);
        Waiting waiting = waitingRepository.findById(request.waitingId()).
                orElseThrow(PubNotFoundException::new);

        if (!waiting.getUser().equals(user)) {
            throw new WaitingNotFoundException();
        }

        cancelWaiting(waiting, waiting.getPub());
        return new WaitingResponse(true, SuccessCode.WAITING_CANCEL_SUCCESS.getMessage());
    }

    private void cancelWaiting(Waiting waiting, Pub pub) {
        waiting.cancel();
        waitingRepository.save(waiting);

        pub.decreaseWaitingCount();
        pubRepository.save(pub);
    }
}
