package site.gachontable.gachontablebe.global.jwt.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class MalformedTokenException extends ServiceException {
    public MalformedTokenException() {
        super(ErrorCode.MALFORMED_TOKEN);
    }
}
