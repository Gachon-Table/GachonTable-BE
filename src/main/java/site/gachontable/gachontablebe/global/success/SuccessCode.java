package site.gachontable.gachontablebe.global.success;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    FORCE_CANCEL_SUCCESS(200, "FORCE_CANCEL_SUCCESS", "예약 강제 취소에 성공하였습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}