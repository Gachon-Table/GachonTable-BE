package site.gachontable.gachontablebe.domain.pub.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.List;

@Entity(name = "pub")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private List<Waiting> waitingQueue;
}
