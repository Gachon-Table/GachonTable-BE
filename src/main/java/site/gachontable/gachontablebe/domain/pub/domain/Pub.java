package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.List;
import java.util.Queue;

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

    @Column(nullable = true)
    private String pubLoc;

    @Column(nullable = false)
    private String pubThumbnail;

    @OneToMany
    @Column(nullable = false)
    private List<Menu> menus;

    @Column(nullable = false)
    private Boolean openStatus;

    @OneToMany
    @Column(nullable = false)
    private Queue<Waiting> waitingQueue;

    public void updateQueue(Waiting waiting) {
        this.waitingQueue.add(waiting);
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
                             Queue<Waiting> waitingQueue) {
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
                .waitingQueue(waitingQueue)
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
               Queue<Waiting> waitingQueue) {
        this.pubName = pubName;
        this.oneLiner = oneLiner;
        this.pubTel = pubTel;
        this.studentCard = studentCard;
        this.representativeMenu = representativeMenu;
        this.pubLoc = pubLoc;
        this.pubThumbnail = pubThumbnail;
        this.menus = menus;
        this.openStatus = openStatus;
        this.waitingQueue = waitingQueue;
    }

}
