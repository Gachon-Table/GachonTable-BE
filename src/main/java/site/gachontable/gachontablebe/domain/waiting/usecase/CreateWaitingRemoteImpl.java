package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.WaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;

@Component
@RequiredArgsConstructor
public class CreateWaitingRemoteImpl implements CreateWaiting {
    private final PubRepository pubRepository;

    @Override
    public WaitingResponse execute(User user, Integer pubId, WaitingRequest request) {
        String username = user.getUserName();
        Pub pub = pubRepository.findByPubId(pubId).orElseThrow(PubNotFoundException::new);

        if (!pub.getOpenStatus()) {
            throw new PubNotFoundException();
        }

        return null;
    }
}
