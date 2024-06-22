package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Component
@RequiredArgsConstructor
public class CreateWaitingRemoteImpl implements CreateWaiting {
    private final PubRepository pubRepository;
    private final WaitingRepository waitingRepository;

    @Override
    public WaitingResponse execute(User user, RemoteWaitingRequest request) {
        Pub pub = pubRepository.findByPubId(request.pubId()).orElseThrow(PubNotFoundException::new);

        if (!pub.getOpenStatus()) {
            throw new PubNotOpenException();
        }
        Waiting waiting = Waiting.create(Position.REMOTE, request.headCount(), Status.WAITING, user, pub);
        waitingRepository.save(waiting);

        pub.updateQueue(waiting);

        return new WaitingResponse(true, SuccessCode.ENTERED_SUCCESS.getMessage());
    }
}
