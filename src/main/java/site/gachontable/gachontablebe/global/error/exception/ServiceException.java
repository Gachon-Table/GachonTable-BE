package site.gachontable.gachontablebe.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.gachontable.gachontablebe.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}