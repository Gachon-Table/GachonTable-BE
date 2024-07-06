package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.OrderResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetOrderImpl implements GetOrder {
    private final WaitingRepository waitingRepository;

    @Override
    public List<OrderResponse> execute(User user) {
        return getPubsFromWaitings(user).stream()
                .flatMap(pub -> {
                    List<Waiting> waitings = waitingRepository.findAllByPubAndWaitingStatusOrWaitingStatus(pub, Status.WAITING, Status.AVAILABLE);

                    return getOrderResponse(user, pub, waitings);
                })
                .toList();
    }

    private static Stream<OrderResponse> getOrderResponse(User user, Pub pub, List<Waiting> waitings) {
        return waitings.stream()
                .filter(waiting -> Optional.ofNullable(waiting.getUser())
                        .map(waitingUser -> !waitingUser.equals(user))
                        .isPresent())
                .map(waiting -> OrderResponse.of(pub.getPubName(),
                        waiting.getWaitingStatus().getStatusKo(),
                        waitings.indexOf(waiting) + 1));
    }

    private List<Pub> getPubsFromWaitings(User user) {
        return waitingRepository.findAllByUser(user).stream()
                .map(Waiting::getPub)
                .distinct()
                .toList();
    }
}
