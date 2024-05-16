package site.gachontable.gachontablebe.domain.admin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "admin")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
