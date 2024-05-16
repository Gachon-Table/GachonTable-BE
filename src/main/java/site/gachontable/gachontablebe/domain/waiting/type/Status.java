package site.gachontable.gachontablebe.domain.waiting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ENTERED("입장 완료"),
    CANCELED("취소 완료"),
    WAITING("대기 중");

    private final String statusKo;
}
