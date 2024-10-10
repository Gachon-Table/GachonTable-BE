package site.gachontable.gachontablebe.domain.menu.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

@Entity(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pub pub;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String oneLiner;

    @Column
    private String thumbnail;

    public static Menu create(Pub pub, String menuName, String price, String oneLiner, String thumbnail) {
        return Menu.builder()
                .pub(pub)
                .menuName(menuName)
                .price(price)
                .oneLiner(oneLiner)
                .thumbnail(thumbnail)
                .build();
    }

    @Builder
    public Menu(Pub pub, String menuName, String price, String oneLiner, String thumbnail) {
        this.pub = pub;
        this.menuName = menuName;
        this.price = price;
        this.oneLiner = oneLiner;
        this.thumbnail = thumbnail;
    }

    public void update(String menuName, String price, String oneLiner, String thumbnail) {
        this.thumbnail = thumbnail;
        this.menuName = menuName;
        this.price = price;
        this.oneLiner = oneLiner;
    }
}
