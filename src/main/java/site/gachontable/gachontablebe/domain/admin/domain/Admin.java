package site.gachontable.gachontablebe.domain.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

@Entity(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false)
    private String adminPassword;

    @Column(columnDefinition = "char(13)", nullable = false)
    private String adminTel;

    @ManyToOne
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;

    public static Admin create(String adminName, String adminPassword, String adminTel, Pub pub) {
        return Admin.builder()
                .adminName(adminName)
                .adminPassword(adminPassword)
                .adminTel(adminTel)
                .pub(pub)
                .build();
    }

    @Builder
    public Admin(String adminName, String adminPassword, String adminTel, Pub pub) {
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.adminTel = adminTel;
        this.pub = pub;
    }
}
