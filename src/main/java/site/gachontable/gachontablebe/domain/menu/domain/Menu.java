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
    private Integer menuID;

    @ManyToOne
    private Pub pub;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String price;

    @Column
    private String oneLiner;

    public static Menu create(Pub pub, String menuName, String price, String oneLiner) {
        return Menu.builder()
                .pub(pub)
                .menuName(menuName)
                .price(price)
                .oneLiner(oneLiner)
                .build();
    }

    @Builder
    public Menu(Pub pub, String menuName, String price, String oneLiner) {
        this.pub = pub;
        this.menuName = menuName;
        this.price = price;
        this.oneLiner = oneLiner;
    }

    public void update(String price, String oneLiner) {
        this.price = price;
        this.oneLiner = oneLiner;
    }
}
