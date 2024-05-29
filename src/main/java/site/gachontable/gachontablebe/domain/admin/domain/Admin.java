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
    private String adminName;

    @Column(nullable = false)
    private String adminPassword;

    @Column(columnDefinition = "char(13)", nullable = false)
    private String adminTel;

    @Column
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public static Admin create(String adminName, String adminPassword, String adminTel) {
        return Admin.builder()
                .adminName(adminName)
                .adminPassword(adminPassword)
                .adminTel(adminTel)
//                .pub(pub)
                .build();
    }

    @Builder
    public Admin(String adminName, String adminPassword, String adminTel, Pub pub) {
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.adminTel = adminTel;
        this.pub = pub;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
