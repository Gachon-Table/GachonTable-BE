package site.gachontable.gachontablebe.domain.seating.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

@Entity(name = "seating")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatingId;

    @ManyToOne
    @JoinColumn(name = "pub_id")
    private Pub pub;
}
