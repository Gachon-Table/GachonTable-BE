package site.gachontable.gachontablebe.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Table {
    BASIC("4인 테이블 (1~5인)"),
    PARTY("8인 테이블 (5~8인)"),;

    private final String nameKo;
}
