package site.gachontable.gachontablebe.domain.seating.domain.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatingRepository extends JpaRepository<Seating, Long> {

    @Query("SELECT s.exitTime FROM seating s WHERE s.waiting = :waiting")
    Optional<LocalDateTime> findExitTimeByWaiting(Waiting waiting);

    Optional<Seating> findFirstByPubAndSeatingNumAndExitTimeAfter(Pub pub, Integer seatingNum, LocalDateTime now);

    List<Seating> findAllByPubAndExitTimeAfterOrderByExitTime(Pub pub, LocalDateTime now);
}
