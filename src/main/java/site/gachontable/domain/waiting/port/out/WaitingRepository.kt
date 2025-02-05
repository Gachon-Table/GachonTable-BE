package site.gachontable.domain.waiting.port.out

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.domain.waiting.type.Status
import java.util.*

interface WaitingRepository : JpaRepository<Waiting, UUID> {
    fun existsByTelAndPubAndWaitingStatusIn(
        tel: String, pub: Pub, statuses: MutableList<Status>,
    ): Boolean

    @Query(
        "SELECT COUNT(w) FROM waiting w WHERE w.user.userTel = :tel AND " +
                "(w.waitingStatus = :waiting OR w.waitingStatus = :available)"
    )
    fun countByTelAndWaitingStatuses(
        @Param("tel") tel: String,
        @Param("waiting") waiting: Status,
        @Param("available") available: Status,
    ): Int

    fun findAllByPubAndWaitingStatusInOrderByCreatedAtAsc(
        pub: Pub, statuses: MutableList<Status>,
    ): MutableList<Waiting>

    fun findAllByTelAndWaitingStatusInOrderByCreatedAtDesc(
        tel: String?, statuses: MutableList<Status>,
    ): MutableList<Waiting>

    fun findTop3ByPubAndWaitingStatusInOrderByCreatedAtAsc(
        pub: Pub, statuses: MutableList<Status>,
    ): MutableList<Waiting>
}
