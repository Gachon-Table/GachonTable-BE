package site.gachontable.infra.security.jwt.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class ExpiredTokenException extends ServiceException {
    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
