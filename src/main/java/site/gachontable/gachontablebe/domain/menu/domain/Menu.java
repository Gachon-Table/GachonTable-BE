package site.gachontable.gachontablebe.domain.menu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "menu")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuID;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String oneLiner;
}
