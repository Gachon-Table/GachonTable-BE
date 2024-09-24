package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.exception.EmptyWaitingCountException;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotOpenException;
import site.gachontable.gachontablebe.domain.waiting.exception.PubClosedForWaitingException;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "pub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pub {
    private static final Integer MAX_WAITING_COUNT = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pubId;

    @Column(nullable = false)
    private String pubName;

    @Column(nullable = false)
    private String oneLiner;

    @Column(nullable = false)
    private String instagramUrl;

    @Column
    private Boolean studentCard;

    @Column(nullable = false)
    private String representativeMenu;

    @Column(nullable = false)
    private Integer hours;

    @Column
    private String pubLoc;

    @ElementCollection
    @Column(nullable = false)
    private List<String> thumbnails = new ArrayList<>();

    @OneToMany
    @Column(nullable = false)
    private List<Menu> menus = new ArrayList<>();

    @Column(nullable = false)
    private Boolean openStatus;

    @Column(nullable = false)
    private Boolean waitingStatus;

    @Column(nullable = false)
    private Integer waitingCount;

    public static Pub create(String pubName,
                             String oneLiner,
                             String instagramUrl,
                             Boolean studentCard,
                             String representativeMenu,
                             Integer hours,
                             String pubLoc,
                             List<String> thumbnails,
                             List<Menu> menus,
                             Boolean openStatus,
                             Boolean waitingStatus,
                             Integer waitingCount) {
        return Pub.builder()
                .pubName(pubName)
                .oneLiner(oneLiner)
                .instagramUrl(instagramUrl)
                .studentCard(studentCard)
                .representativeMenu(representativeMenu)
                .hours(hours)
                .pubLoc(pubLoc)
                .thumbnails(thumbnails)
                .menus(menus)
                .openStatus(openStatus)
                .waitingStatus(waitingStatus)
                .waitingCount(waitingCount)
                .build();
    }

    @Builder
    public Pub(String pubName,
               String oneLiner,
               String instagramUrl,
               Boolean studentCard,
               String representativeMenu,
               Integer hours,
               String pubLoc,
               List<String> thumbnails,
               List<Menu> menus,
               Boolean openStatus,
               Boolean waitingStatus,
               Integer waitingCount) {
        this.pubName = pubName;
        this.oneLiner = oneLiner;
        this.instagramUrl = instagramUrl;
        this.studentCard = studentCard;
        this.representativeMenu = representativeMenu;
        this.hours = hours;
        this.pubLoc = pubLoc;
        this.thumbnails = thumbnails;
        this.menus = menus;
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
        }
    }

    public void decreaseWaitingCount() {
        validateWaitingCount();
        this.waitingCount -= 1;
    }

    private void validateWaitingCount() {
        if (waitingCount < 1) {
            throw new EmptyWaitingCountException();
        }
    }

    public void updatePubInfo(List<String> thumbnails, List<Menu> menus) {
        this.thumbnails = thumbnails;
        this.menus = menus;
    }

    public void updateOpenStatus(Boolean openStatus) {
        this.openStatus = openStatus;
        this.waitingStatus = openStatus;
        this.waitingCount = 0;
    }

    public void updateWaitingStatus(Boolean waitingStatus) {
        this.waitingStatus = waitingStatus;
        this.waitingCount = 0;
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
