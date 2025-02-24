package site.gachontable.domain.seating.domain

import jakarta.persistence.*

import site.gachontable.presentation.admin.dto.response.SeatingsResponse
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.member.domain.User
import site.gachontable.domain.waiting.domain.Waiting
import site.gachontable.presentation.shared.Table

import java.time.LocalDateTime

@Entity(name = "seating")
class Seating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    val pub: Pub,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_id", nullable = false)
    val waiting: Waiting,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val seatingNum: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tableType: Table,

    @Column(nullable = false)
    var exitTime: LocalDateTime,
) {
    fun updateExitTime() {
        this.exitTime = LocalDateTime.now()
    }

    companion object {
        fun toSeatingResponse(seating: Seating): SeatingsResponse.SeatingResponse =
            SeatingsResponse.SeatingResponse.from(seating)

        fun create(
            seatingNum: Int,
            tableType: Table,
            exitTime: LocalDateTime,
            pub: Pub,
            waiting: Waiting,
            user: User,
        ): Seating {
            return Seating(
                seatingNum = seatingNum,
                tableType = tableType,
                exitTime = exitTime,
                pub = pub,
                waiting = waiting,
                user = user
            )
        }
    }
}
