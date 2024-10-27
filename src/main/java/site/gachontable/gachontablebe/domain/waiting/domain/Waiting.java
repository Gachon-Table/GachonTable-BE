package site.gachontable.gachontablebe.domain.waiting.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.WaitingInfosResponse;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.shared.BaseTimeEntity;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.exception.WaitingCanceledException;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;
import site.gachontable.gachontablebe.domain.shared.Table;

import java.util.UUID;

@Entity(name = "waiting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID waitingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position waitingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Table tableType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status waitingStatus;

    @Column(columnDefinition = "char(16)")
    private String tel;

    public static Waiting create(Position waitingType, Table tableType, Status waitingStatus, String tel, User user, Pub pub) {
        return Waiting.builder()
                .waitingType(waitingType)
                .tableType(tableType)
                .waitingStatus(waitingStatus)
                .tel(tel)
                .user(user)
                .pub(pub)
                .build();
    }

    @Builder
    public Waiting(Position waitingType, Table tableType, Status waitingStatus, String tel, User user, Pub pub) {
        this.waitingType = waitingType;
        this.tableType = tableType;
        this.waitingStatus = waitingStatus;
        this.tel = tel;
        this.user = user;
        this.pub = pub;
    }

    public static WaitingInfosResponse.WaitingInfo toWaitingInfo(Waiting waiting) {
        String username = waiting.getUser().getUsername();
        return WaitingInfosResponse.WaitingInfo.of(username, waiting);
    }

    public void enter() {
        checkCanceled();
        this.waitingStatus = Status.ENTERED;
    }

    public void cancel() {
        this.waitingStatus = Status.CANCELED;
    }

    public void toAvailable() {
        checkCanceled();
        this.waitingStatus = Status.AVAILABLE;
    }

    private void checkCanceled() {
        if (this.waitingStatus == Status.CANCELED) {
            throw new WaitingCanceledException();
        }
    }
}
