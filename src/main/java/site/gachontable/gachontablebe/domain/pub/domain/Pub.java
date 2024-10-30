package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.pub.exception.EmptyWaitingCountException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.waiting.exception.PubClosedForWaitingException;

@Entity(name = "pub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pub {
    private static final Integer MAX_WAITING_COUNT = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pubId;

    @Column(nullable = false)
    private String pubName;

    @Column(nullable = false)
    private String oneLiner;

    @Column(nullable = false)
    private String instagramUrl;

    @Column(nullable = false)
    private Integer minutes;

    @Column(nullable = false)
    private String menuUrl;

    @Column(nullable = false)
    private Boolean openStatus;

    @Column(nullable = false)
    private Boolean waitingStatus;

    @Column(nullable = false)
    private Integer waitingCount;

    @Column(nullable = false)
    private Boolean autoDisabled;

    public static Pub create(String pubName,
                             String oneLiner,
                             String instagramUrl,
                             Integer minutes,
                             String menuUrl,
                             Boolean openStatus,
                             Boolean waitingStatus,
                             Integer waitingCount) {
        return Pub.builder()
                .pubName(pubName)
                .oneLiner(oneLiner)
                .instagramUrl(instagramUrl)
                .minutes(minutes)
                .menuUrl(menuUrl)
                .openStatus(openStatus)
                .waitingStatus(waitingStatus)
                .waitingCount(waitingCount)
                .build();
    }

    @Builder
    private Pub(String pubName,
               String oneLiner,
               String instagramUrl,
               Integer minutes,
               String menuUrl,
               Boolean openStatus,
               Boolean waitingStatus,
               Integer waitingCount) {
        this.pubName = pubName;
        this.oneLiner = oneLiner;
        this.instagramUrl = instagramUrl;
        this.minutes = minutes;
        this.menuUrl = menuUrl;
        this.openStatus = openStatus;
        this.waitingStatus = waitingStatus;
        this.waitingCount = waitingCount;
    }

    public void increaseWaitingCount() {
        this.waitingCount += 1;
        checkMaxWaitingCount();
    }

    private void checkMaxWaitingCount() {
        if (this.waitingCount >= MAX_WAITING_COUNT) {
            this.waitingStatus = false;
            this.autoDisabled = true;
        }
    }

    public void decreaseWaitingCount() {
        validateWaitingCount();
        validateCanUpdateWaitingStatusToTrue();
        this.waitingCount -= 1;
    }

    private void validateCanUpdateWaitingStatusToTrue() {
        if (this.autoDisabled && !this.waitingStatus && this.waitingCount <= MAX_WAITING_COUNT) {
            this.waitingStatus = true;
        }
    }

    private void validateWaitingCount() {
        if (waitingCount < 1) {
            throw new EmptyWaitingCountException();
        }
    }

    public void updateOpenStatus(Boolean openStatus) {
        this.openStatus = openStatus;
        this.waitingStatus = openStatus;
        this.waitingCount = 0;
    }

    public void updateWaitingStatus(Boolean waitingStatus) {
        this.waitingStatus = waitingStatus;
        this.autoDisabled = false;
    }

    public void checkStatus() {
        if (!this.openStatus) {
            throw new PubNotOpenException();
        }

        if (!this.waitingStatus) {
            throw new PubClosedForWaitingException();
        }
    }
}
