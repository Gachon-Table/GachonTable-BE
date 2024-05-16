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

    @Column(columnDefinition = "char(13)", nullable = false)
    private String userTel;

    @Column(nullable = false)
    private Byte queueingCount;

    @Column(nullable = false)
    private String refreshToken;

    public static User create(String userName, String userTel, Byte queueingCount, String refreshToken) {
        return User.builder()
                .userName(userName)
                .userTel(userTel)
                .queueingCount(queueingCount)
                .refreshToken(refreshToken)
                .build();
    }

    @Builder
    public User(String userName, String userTel, Byte queueingCount, String refreshToken) {
        this.userName = userName;
        this.userTel = userTel;
        this.queueingCount = queueingCount;
        this.refreshToken = refreshToken;
    }
}
