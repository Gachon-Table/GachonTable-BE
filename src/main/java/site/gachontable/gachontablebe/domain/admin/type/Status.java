package site.gachontable.gachontablebe.domain.admin.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ENTER("입장"),
    CALL("호출");

    private final String statusKo;
}
