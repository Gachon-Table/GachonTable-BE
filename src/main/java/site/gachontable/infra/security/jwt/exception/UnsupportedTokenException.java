package site.gachontable.infra.security.jwt.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class UnsupportedTokenException extends ServiceException {
    public UnsupportedTokenException() {
        super(ErrorCode.UNSUPPORTED_TOKEN);
    }
}
