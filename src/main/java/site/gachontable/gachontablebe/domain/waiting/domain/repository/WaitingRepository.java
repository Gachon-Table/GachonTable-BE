package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.UUID;

public interface WaitingRepository extends JpaRepository<Waiting, UUID> {

    boolean existsByTelAndPubAndWaitingStatusOrWaitingStatus(String tel, Pub pub, Status waiting, Status available);

    @Query("SELECT COUNT(w) FROM waiting w WHERE w.user.userTel = :tel AND (w.waitingStatus = :waiting OR w.waitingStatus = :available)")
    int countByTelAndWaitingStatuses(@Param("tel") String tel, @Param("status1") Status waiting, @Param("status2") Status available);

    @Query("SELECT DISTINCT w.pub FROM waiting w WHERE w.user.userTel = :tel")
    List<Pub> findDistinctPubsByTel(@Param("tel") String tel);

    List<Waiting> findAllByPubAndWaitingStatusOrWaitingStatusOrderByCreatedAtAsc(Pub pub, Status waiting, Status available);

    List<Waiting> findAllByUserAndWaitingStatusOrWaitingStatus(User user, Status entered, Status canceled);
}
