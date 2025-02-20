package site.gachontable.infra.security.jwt.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class MalformedTokenException extends ServiceException {
    public MalformedTokenException() {
        super(ErrorCode.MALFORMED_TOKEN);
    }
}
