package site.gachontable.infra.security.jwt.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class InvalidTokenException extends ServiceException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
