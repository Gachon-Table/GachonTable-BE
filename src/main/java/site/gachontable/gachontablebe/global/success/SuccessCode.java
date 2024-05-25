package site.gachontable.gachontablebe.global.success;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    ENTERED_SUCCESS(200, "ENTERED_SUCCESS", "입장 완료에 성공하였습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}