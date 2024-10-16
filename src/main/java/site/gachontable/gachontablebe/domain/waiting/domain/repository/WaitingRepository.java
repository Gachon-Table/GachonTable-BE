package site.gachontable.gachontablebe.domain.waiting.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.List;
import java.util.UUID;

public interface WaitingRepository extends JpaRepository<Waiting, UUID> {

    boolean existsByTelAndPubAndWaitingStatusIn(String tel, Pub pub, List<Status> statuses);

    @Query("SELECT COUNT(w) FROM waiting w WHERE w.user.userTel = :tel AND " +
            "(w.waitingStatus = :waiting OR w.waitingStatus = :available)")
    int countByTelAndWaitingStatuses(@Param("tel") String tel,
                                     @Param("waiting") Status waiting,
                                     @Param("available") Status available);

    @Query("SELECT DISTINCT w.pub FROM waiting w WHERE w.user.userTel = :tel")
    List<Pub> findDistinctPubsByTel(@Param("tel") String tel);

    List<Waiting> findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(Pub pub, List<Status> statuses);

    List<Waiting> findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(String tel, List<Status> statuses);
}
