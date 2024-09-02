package site.gachontable.gachontablebe.domain.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.UUID;

@Entity(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID adminId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String adminPassword;

    @Column(columnDefinition = "char(16)", nullable = false)
    private String adminTel;

    @Column
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public static Admin create(String username, String adminPassword, String adminTel, Pub pub) {
        return Admin.builder()
                .username(username)
                .adminPassword(adminPassword)
                .adminTel(adminTel)
                .pub(pub)
                .build();
    }

    @Builder
    public Admin(String username, String adminPassword, String adminTel, Pub pub) {
        this.username = username;
        this.adminPassword = adminPassword;
        this.adminTel = adminTel;
        this.pub = pub;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
