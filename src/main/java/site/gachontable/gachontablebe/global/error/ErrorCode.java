package site.gachontable.gachontablebe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMPTY_QUEUEING_COUNT(400, "EMPTY_QUEUEING_COUNT", "사용자의 대기열이 비어있습니다."),
    EMPTY_WAITING_COUNT(400, "EMPTY_WAITING_COUNT", "주점에 대기열이 없습니다."),
    INVALID_TOKEN(401, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(403, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    PASSWORD_NOT_MATCH(403, "PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다."),
    PUB_MISMATCH(403, "PUB_MISMATCH", "요청하는 주점이 일치하지 않습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    ADMIN_NOT_FOUND(404, "ADMIN_NOT_FOUND", "존재하지 않는 관리자입니다."),
    WAITING_NOT_FOUND(404, "WAITING_NOT_FOUND", "존재하지 않는 대기열입니다."),
    PUB_NOT_FOUND(404, "PUB_NOT_FOUND", "존재하지 않는 술집입니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),
    PUB_NOT_OPEN(503, "PUB_NOT_OPEN", "현재 오픈되어 있지 않은 주점입니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}