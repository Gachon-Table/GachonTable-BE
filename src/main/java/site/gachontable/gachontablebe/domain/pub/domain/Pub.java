package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;

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

    @Column(columnDefinition = "char(13)", nullable = false)
    private String pubTel;

    @Column(nullable = false)
    private Boolean studentCard;

    @Column(nullable = false)
    private String representativeMenu;

    @Column
    private String pubLoc;

    @Column(nullable = false)
    private String pubThumbnail;

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
                             String pubTel,
                             Boolean studentCard,
                             String representativeMenu,
                             String pubLoc,
                             String pubThumbnail,
                             List<Menu> menus,
                             Boolean openStatus,
                             Integer waitingCount) {
        return Pub.builder()
                .pubName(pubName)
                .oneLiner(oneLiner)
                .pubTel(pubTel)
                .studentCard(studentCard)
                .representativeMenu(representativeMenu)
                .pubLoc(pubLoc)
                .pubThumbnail(pubThumbnail)
                .menus(menus)
                .openStatus(openStatus)
                .waitingCount(waitingCount)
                .build();
    }

    @Builder
    public Pub(String pubName,
               String oneLiner,
               String pubTel,
               Boolean studentCard,
               String representativeMenu,
               String pubLoc,
               String pubThumbnail,
               List<Menu> menus,
               Boolean openStatus,
               Integer waitingCount) {
        this.pubName = pubName;
        this.oneLiner = oneLiner;
        this.pubTel = pubTel;
        this.studentCard = studentCard;
        this.representativeMenu = representativeMenu;
        this.pubLoc = pubLoc;
        this.pubThumbnail = pubThumbnail;
        this.menus = menus;
        this.openStatus = openStatus;
        this.waitingCount = waitingCount;
    }

}
