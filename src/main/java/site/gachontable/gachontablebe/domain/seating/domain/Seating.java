package site.gachontable.gachontablebe.domain.seating.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.shared.Table;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Table tableType;

    @Column(nullable = false)
    private LocalDateTime exitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_id")
    private Waiting waiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Seating create(Integer seatingNum, Table tableType, LocalDateTime exitTime, Pub pub, Waiting waiting, User user) {
        return Seating.builder()
                .seatingNum(seatingNum)
                .tableType(tableType)
                .exitTime(exitTime)
                .pub(pub)
                .waiting(waiting)
                .user(user)
                .build();
    }

    @Builder
    public Seating(Integer seatingNum, Table tableType, LocalDateTime exitTime, Pub pub, Waiting waiting, User user) {
        this.seatingNum = seatingNum;
        this.tableType = tableType;
        this.exitTime = exitTime;
        this.pub = pub;
        this.waiting = waiting;
        this.user = user;
    }

    public static SeatingsResponse.SeatingResponse toSeatingResponse(Seating seating) {
        return SeatingsResponse.SeatingResponse.from(seating);
    }

    public void updateExitTime() {
        this.exitTime = LocalDateTime.now();
    }
}
