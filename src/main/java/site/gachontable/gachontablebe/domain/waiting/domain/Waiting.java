package site.gachontable.gachontablebe.domain.waiting.domain;

import jakarta.persistence.*;
import lombok.*;
import site.gachontable.gachontablebe.domain.shared.BaseTimeEntity;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

@Entity(name = "waiting")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Waiting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position waitingType;

    @Column(nullable = false)
    private Integer headCount;

    @Column(nullable = true)
    private Status waitingStatus;.
}
