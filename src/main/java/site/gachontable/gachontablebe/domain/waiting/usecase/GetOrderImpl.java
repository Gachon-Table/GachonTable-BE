package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.OrderResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetOrderImpl implements GetOrder {
    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderResponse> execute(AuthDetails authDetails) {
        User user = userRepository.findById(authDetails.getUuid()).orElseThrow(UserNotFoundException::new);

        return getPubsFromWaitings(user).stream()
                .flatMap(pub -> {
                    List<Waiting> waitings = waitingRepository.findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(pub, Status.WAITING, Status.AVAILABLE);

                    return getOrderResponse(user, pub, waitings);
                })
                .toList();
    }

    private static Stream<OrderResponse> getOrderResponse(User user, Pub pub, List<Waiting> waitings) {
        return waitings.stream()
                .filter(waiting -> waiting.matchesUser(user))
                .map(waiting -> OrderResponse.of(
                        waiting.getWaitingId(),
                        pub.getPubName(),
                        waiting.getWaitingStatus().getStatusKo(),
                        waitings.indexOf(waiting) + 1));
    }

    private List<Pub> getPubsFromWaitings(User user) {
        return waitingRepository.findAllByTel(user.getUserTel()).stream()
                .map(Waiting::getPub)
                .distinct()
                .toList();
    }
}
