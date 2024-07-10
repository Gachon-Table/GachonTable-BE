package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Optional<Waiting> findByUser(User user);
    List<Waiting> findAllByUser(User user);

    List<Waiting> findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(Pub pub, Status waiting, Status available);
    List<Waiting> findAllByUserAndWaitingStatusOrWaitingStatus(User user, Status entered, Status canceled);
}