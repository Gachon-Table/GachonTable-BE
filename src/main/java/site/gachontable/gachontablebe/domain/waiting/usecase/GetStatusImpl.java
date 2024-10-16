package site.gachontable.gachontablebe.domain.waiting.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.domain.repository.WaitingRepository;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetStatusImpl implements GetStatus {

    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StatusResponse> execute(AuthDetails authDetails) {
        User user = userRepository.findById(authDetails.getUuid()).orElseThrow(UserNotFoundException::new);

        return getPubsFromWaitings(user).stream()
                .flatMap(pub -> {
                    List<Waiting> waitings = waitingRepository
                            .findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
                                    pub, Arrays.asList(Status.WAITING, Status.AVAILABLE));

                    return getStatusResponse(user, pub, waitings);
                })
                .toList();
    }

    private Stream<StatusResponse> getStatusResponse(User user, Pub pub, List<Waiting> waitings) {
        return waitings.stream()
                .filter(waiting -> waiting.matchesUser(user))
                .map(waiting -> StatusResponse.of(waiting, pub, waitings.indexOf(waiting) + 1));
    }

    private List<Pub> getPubsFromWaitings(User user) {
        return waitingRepository.findDistinctPubsByTel(user.getUserTel());
    }
}
