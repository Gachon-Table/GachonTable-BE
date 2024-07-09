package site.gachontable.gachontablebe.domain.waiting.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.shared.BaseTimeEntity;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.PubWaitingListResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.util.Objects;

@Entity(name = "waiting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position waitingType;

    @Column(nullable = false)
    private Integer headCount;

    @Enumerated(EnumType.STRING)
    @Column
    private Status waitingStatus;

    @Column
    private String tel; // 비회원 식별을 위한 휴대폰 번호

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;

    public boolean matchesUser(User user) {
        return Objects.equals(this.user, user);
    }

    public static Waiting create(Position waitingType, Integer headCount, Status waitingStatus, String tel, User user, Pub pub) {
        return Waiting.builder()
                .waitingType(waitingType)
                .headCount(headCount)
                .waitingStatus(waitingStatus)
                .tel(tel)
                .user(user)
                .pub(pub)
                .build();
    }

    @Builder
    public Waiting(Position waitingType, Integer headCount, Status waitingStatus, String tel, User user, Pub pub) {
        this.waitingType = waitingType;
        this.headCount = headCount;
        this.waitingStatus = waitingStatus;
        this.tel = tel;
        this.user = user;
        this.pub = pub;
    }

    public static PubWaitingListResponse.WaitingInfo toWaitingInfo(Waiting waiting) {
        String username = (waiting.getUser() == null) ? waiting.getTel().substring(9) : waiting.getUser().getUsername();
        return PubWaitingListResponse.WaitingInfo.of(username, waiting);
    }

    public void enter() {
        this.waitingStatus = Status.ENTERED;
    }

    public void cancel() {
        this.waitingStatus = Status.CANCELED;
    }
 }
