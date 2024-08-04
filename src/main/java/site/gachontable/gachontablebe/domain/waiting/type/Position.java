package site.gachontable.gachontablebe.domain.waiting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    ONSITE("현장"),
    REMOTE("원격"),
    CANCEL("취소");

    private final String positionKo;
}
