package site.gachontable.gachontablebe.domain.waiting.usecase;

import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.WaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;

@Component
public class CreateWaitingRemoteImpl implements CreateWaiting{

    @Override
    public WaitingResponse execute(User user, Integer pubId, WaitingRequest request) {
        return null;
    }
}
