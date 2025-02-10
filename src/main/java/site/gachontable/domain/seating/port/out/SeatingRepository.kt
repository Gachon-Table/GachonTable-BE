package site.gachontable.domain.seating.port.out

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.seating.domain.Seating
import site.gachontable.domain.waiting.domain.Waiting
import java.time.LocalDateTime
import java.util.*

interface SeatingRepository : JpaRepository<Seating, Long> {
    @Query("SELECT s.exitTime FROM seating s WHERE s.waiting = :waiting")
    fun findExitTimeByWaiting(waiting: Waiting): Optional<LocalDateTime>

    fun existsByPubAndSeatingNumAndExitTimeAfter(pub: Pub, seatingNum: Int, now: LocalDateTime): Boolean

    fun findAllByPubAndExitTimeAfterOrderByExitTime(pub: Pub, now: LocalDateTime): MutableList<Seating>
}
