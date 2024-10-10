package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class WaitingNotFoundException extends ServiceException {
    public WaitingNotFoundException() {
        super(ErrorCode.WAITING_NOT_FOUND);
    }
}
