package site.gachontable.gachontablebe.domain.seating.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(nullable = false)
    private Integer seatingNum;

    @Column(nullable = false)
    private Boolean allocated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public static Seating create(Integer seatingNum, Boolean allocated, Pub pub) {
        return Seating.builder()
                .seatingNum(seatingNum)
                .allocated(allocated)
                .pub(pub)
                .build();
    }

    @Builder
    public Seating(Integer seatingNum, Boolean allocated, Pub pub) {
        this.seatingNum = seatingNum;
        this.allocated = allocated;
        this.pub = pub;
    }
}
