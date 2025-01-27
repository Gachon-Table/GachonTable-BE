package site.gachontable.independent.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.gachontable.independent.type.ErrorCode;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
