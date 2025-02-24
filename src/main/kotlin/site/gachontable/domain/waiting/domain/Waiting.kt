package site.gachontable.domain.waiting.domain

import jakarta.persistence.*
import site.gachontable.presentation.admin.dto.response.WaitingInfosResponse
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.waiting.exception.WaitingCanceledException
import site.gachontable.domain.waiting.type.Position
import site.gachontable.domain.waiting.type.Status
import site.gachontable.infra.database.basetime.BaseTimeEntity
import site.gachontable.presentation.shared.Table

import java.util.UUID

@Entity(name = "waiting")
class Waiting(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    val pub: Pub,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val waitingType: Position,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tableType: Table,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var waitingStatus: Status,

    @Column(columnDefinition = "char(16)")
    val tel: String,
) : BaseTimeEntity() {
    fun enter() {
        checkCanceled()
        this.waitingStatus = Status.ENTERED
    }

    fun cancel() {
        checkCanceled()
        this.waitingStatus = Status.CANCELED
    }

    fun toAvailable() {
        checkCanceled()
        this.waitingStatus = Status.AVAILABLE
    }

    private fun checkCanceled() {
        if (this.waitingStatus == Status.CANCELED) {
            throw WaitingCanceledException()
        }
    }

    companion object {
        fun toWaitingInfo(waiting: Waiting): WaitingInfosResponse.WaitingInfo {
            val username = waiting.user.username
            return WaitingInfosResponse.WaitingInfo.of(username, waiting)
        }

        fun create(
            waitingType: Position,
            tableType: Table,
            waitingStatus: Status,
            tel: String,
            user: User,
            pub: Pub,
        ): Waiting {
            return Waiting(
                waitingType = waitingType,
                tableType = tableType,
                waitingStatus = waitingStatus,
                tel = tel,
                user = user,
                pub = pub
            )
        }
    }
}
