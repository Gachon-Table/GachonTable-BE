package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingHistoryResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWaitingHistory {
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;

    public List<WaitingHistoryResponse> excute(AuthDetails authDetails) {
        User user = userRepository.findById(authDetails.getUuid()).orElseThrow(UserNotFoundException::new);

        List<Waiting> waitings = waitingRepository.findAllByUserAndWaitingStatusOrWaitingStatus(user, Status.ENTERED,Status.CANCELED);
        return waitings.stream()
                .map(WaitingHistoryResponse::from)
                .toList();
    }
}
