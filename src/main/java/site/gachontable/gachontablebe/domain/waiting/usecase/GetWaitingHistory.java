package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingHistoryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWaitingHistory {
    private final WaitingRepository waitingRepository;
    public List<WaitingHistoryResponse> excute(User user) {
        List<Waiting> waitings = waitingRepository.findAllByUser(user);
        return waitings.stream()
                .map(WaitingHistoryResponse::from)
                .toList();
    }
}
