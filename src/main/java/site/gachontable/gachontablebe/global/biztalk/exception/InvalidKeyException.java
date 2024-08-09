package site.gachontable.gachontablebe.global.biztalk.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class InvalidKeyException extends ServiceException {
    public InvalidKeyException() {
        super(ErrorCode.INVALID_KEY);
    }
}
