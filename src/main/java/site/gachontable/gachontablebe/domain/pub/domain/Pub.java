package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.exception.EmptyWaitingCountException;

import java.util.List;

@Entity(name = "pub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pub {

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
    private Boolean studentCard;

    @Column(nullable = false)
    private String representativeMenu;

    @Column
    private String pubLoc;

    @ElementCollection
    @Column(nullable = false)
    private List<String> thumbnails;

    @OneToMany
    @Column(nullable = false)
    private List<Menu> menus;

    @Column(nullable = false)
    private Boolean openStatus;

    @Column(nullable = false)
    private Integer waitingCount;

    public void updateWaitingCount(Integer waitingCount) {
        this.waitingCount = waitingCount;
    }

    public static Pub create(String pubName,
                             String oneLiner,
                             String instagramUrl,
                             Boolean studentCard,
                             String representativeMenu,
                             String pubLoc,
                             List<String> thumbnails,
                             List<Menu> menus,
                             Boolean openStatus,
                             Integer waitingCount) {
        return Pub.builder()
                .pubName(pubName)
                .oneLiner(oneLiner)
                .instagramUrl(instagramUrl)
                .studentCard(studentCard)
                .representativeMenu(representativeMenu)
                .pubLoc(pubLoc)
                .thumbnails(thumbnails)
                .menus(menus)
                .openStatus(openStatus)
                .waitingCount(waitingCount)
                .build();
    }

    @Builder
    public Pub(String pubName,
               String oneLiner,
               String instagramUrl,
               Boolean studentCard,
               String representativeMenu,
               String pubLoc,
               List<String> thumbnails,
               List<Menu> menus,
               Boolean openStatus,
               Integer waitingCount) {
        this.pubName = pubName;
        this.oneLiner = oneLiner;
        this.instagramUrl = instagramUrl;
        this.studentCard = studentCard;
        this.representativeMenu = representativeMenu;
        this.pubLoc = pubLoc;
        this.thumbnails = thumbnails;
        this.menus = menus;
        this.openStatus = openStatus;
        this.waitingCount = waitingCount;
    }

    public void decreaseWaitingCount() {
        if (this.getWaitingCount() == 0) {
            throw new EmptyWaitingCountException();
        }

        this.waitingCount -= this.waitingCount;
    }

    public void updatePubInfo(List<String> thumbnails, String oneLiner, Boolean studentCard, List<Menu> menus) {
        this.thumbnails = thumbnails;
        this.oneLiner = oneLiner;
        this.studentCard = studentCard;
        this.menus = menus;
    }

    public void updateOpenStatus(Boolean openStatus) {
        this.openStatus = openStatus;
    }
}
