package site.gachontable.gachontablebe.global.jwt.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class UnsupportedTokenException extends ServiceException {
    public UnsupportedTokenException() {
        super(ErrorCode.UNSUPPORTED_TOKEN);
    }
}
