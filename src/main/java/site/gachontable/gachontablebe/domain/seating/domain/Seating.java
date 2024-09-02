package site.gachontable.gachontablebe.domain.seating.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private LocalDateTime exitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_id")
    private Waiting waiting;

    public static Seating create(Integer seatingNum, Boolean allocated, LocalDateTime exitTime, Pub pub, Waiting waiting) {
        return Seating.builder()
                .seatingNum(seatingNum)
                .allocated(allocated)
                .exitTime(exitTime)
                .pub(pub)
                .waiting(waiting)
                .build();
    }

    @Builder
    public Seating(Integer seatingNum, Boolean allocated, LocalDateTime exitTime, Pub pub, Waiting waiting) {
        this.seatingNum = seatingNum;
        this.allocated = allocated;
        this.exitTime = exitTime;
        this.pub = pub;
        this.waiting = waiting;
    }

    public static SeatingsResponse.SeatingResponse toSeatingResponse(Seating seating) {
        return SeatingsResponse.SeatingResponse.from(seating);
    }
}
