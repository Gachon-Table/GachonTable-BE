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

    private String password;

    @Column(columnDefinition = "char(13)")
    private String userTel;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User createForTest(String userName, String password, String userTel) {
        return User.builder()
                .userName(userName)
                .password(password)
                .userTel(userTel)
                .build();
    }

    public static User create(String userName, String userTel) {
        return User.builder()
                .userName(userName)
                .userTel(userTel)
                .build();
    }

    @Builder
    public User(String userName, String password, String userTel) {
        this.userName = userName;
        this.password = password;
        this.userTel = userTel;
    }
}
