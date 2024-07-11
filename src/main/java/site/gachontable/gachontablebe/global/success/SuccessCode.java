package site.gachontable.gachontablebe.global.success;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    ENTERED_SUCCESS(200, "ENTERED_SUCCESS", "입장 완료에 성공하였습니다."),
    REMOTE_WAITING_SUCCESS(201, "REMOTE_WAITING_SUCCESS", "원격 웨이팅에 성공하였습니다."),
    ONSITE_WAITING_SUCCESS(201, "ONSITE_WAITING_SUCCESS", "현장 웨이팅에 성공하였습니다."),
    USER_CALL_SUCCESS(201, "USER_CALL_SUCCESS", "사용자 호출에 성공하였습니다."),
    WAITING_CANCEL_SUCCESS(201, "WAITING_CANCEL_SUCCESS", "웨이팅 취소에 성공하였습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}