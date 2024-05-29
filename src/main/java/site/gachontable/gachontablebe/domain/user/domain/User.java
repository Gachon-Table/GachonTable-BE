package site.gachontable.gachontablebe.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "char(13)", nullable = false)
    private String userTel;

    @Column(nullable = false)
    private Byte queueingCount;

    @Column(nullable = false)
    private String refreshToken;

    public void decreaseQueueingCount() {
        this.queueingCount = (byte) (this.queueingCount - 1);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User create(String userName, String password, String userTel, Byte queueingCount) {
        return User.builder()
                .userName(userName)
                .password(password)
                .userTel(userTel)
                .queueingCount(queueingCount)
                .build();
    }

    @Builder
    public User(String userName, String password, String userTel, Byte queueingCount) {
        this.userName = userName;
        this.password = password;
        this.userTel = userTel;
        this.queueingCount = queueingCount;
    }
}
