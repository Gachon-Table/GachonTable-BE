package site.gachontable.gachontablebe.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.shared.BaseTimeEntity;

import java.util.UUID;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Column(columnDefinition = "char(16)", nullable = false)
    private String userTel;

    @Column
    private String refreshToken;

    public static User createForTest(String username, String password, String userTel) {
        return User.builder()
                .username(username)
                .password(password)
                .userTel(userTel)
                .build();
    }

    public static User create(String username, String userTel) {
        return User.builder()
                .username(username)
                .userTel(userTel)
                .build();
    }

    @Builder
    private User(String username, String password, String userTel) {
        this.username = username;
        this.password = password;
        this.userTel = userTel;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
