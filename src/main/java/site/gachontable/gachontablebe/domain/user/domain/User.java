package site.gachontable.gachontablebe.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "user")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
