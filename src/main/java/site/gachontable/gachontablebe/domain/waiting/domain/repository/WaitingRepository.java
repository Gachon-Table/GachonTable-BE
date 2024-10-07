package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WaitingRepository extends JpaRepository<Waiting, UUID> {

    Optional<Waiting> findByTelAndPubAndWaitingStatusOrWaitingStatus(String tel, Pub pub, Status waiting, Status available);

    List<Waiting> findAllByTelAndWaitingStatusOrWaitingStatus(String tel, Status waiting, Status available);

    List<Waiting> findAllByTel(String tel);

    List<Waiting> findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(Pub pub, Status waiting, Status available);

    List<Waiting> findAllByUserAndWaitingStatusOrWaitingStatus(User user, Status entered, Status canceled);
}
