package site.gachontable.domain.pub.domain

import jakarta.persistence.*
import site.gachontable.domain.pub.exception.EmptyWaitingCountException
import site.gachontable.domain.pub.exception.PubNotOpenException
import site.gachontable.domain.waiting.exception.PubClosedForWaitingException

@Entity(name = "pub")
class Pub(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @Column(nullable = false)
    val pubName: String,

    @Column(nullable = false)
    val oneLiner: String,

    @Column(nullable = false)
    val instagramUrl: String,

    @Column(nullable = false)
    val minutes: Int,

    @Column(nullable = false)
    val menuUrl: String,

    @Column(nullable = false)
    var openStatus: Boolean,

    @Column(nullable = false)
    var waitingStatus: Boolean,

    @Column(nullable = false)
    var waitingCount: Int,

    @Column(nullable = false)
    var autoDisabled: Boolean,
) {
    fun increaseWaitingCount() {
        this.waitingCount += 1
        checkMaxWaitingCount()
    }

    private fun checkMaxWaitingCount() {
        if (this.waitingCount >= MAX_WAITING_COUNT) {
            this.waitingStatus = false
            this.autoDisabled = true
        }
    }

    fun decreaseWaitingCount() {
        validateWaitingCount()
        validateCanUpdateWaitingStatusToTrue()
        this.waitingCount -= 1
    }

    fun validateCanUpdateWaitingStatusToTrue() {
        if (this.autoDisabled && !this.waitingStatus && this.waitingCount <= MAX_WAITING_COUNT) {
            this.waitingStatus = true
        }
    }

    private fun validateWaitingCount() {
        if (waitingCount < 1) {
            throw EmptyWaitingCountException()
        }
    }

    fun updateOpenStatus(openStatus: Boolean) {
        this.openStatus = openStatus
        this.waitingStatus = openStatus
        this.waitingCount = 0
    }

    fun updateWaitingStatus(waitingStatus: Boolean) {
        this.waitingStatus = waitingStatus
        this.autoDisabled = false
    }

    fun checkStatus() {
        if (!this.openStatus) {
            throw PubNotOpenException()
        }

        if (!this.waitingStatus) {
            throw PubClosedForWaitingException()
        }
    }

    companion object {
        fun create(
            pubName: String,
            oneLiner: String,
            instagramUrl: String,
            minutes: Int,
            menuUrl: String,
            openStatus: Boolean,
            waitingStatus: Boolean,
            waitingCount: Int,
        ): Pub {
            return Pub(
                pubName = pubName,
                oneLiner = oneLiner,
                instagramUrl = instagramUrl,
                minutes = minutes,
                menuUrl = menuUrl,
                openStatus = openStatus,
                waitingStatus = waitingStatus,
                waitingCount = waitingCount,
                autoDisabled = false
            )
        }

        private const val MAX_WAITING_COUNT: Int = 50
    }
}
