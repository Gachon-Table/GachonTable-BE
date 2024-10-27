package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class WaitingCanceledException extends ServiceException {
    public WaitingCanceledException() {
        super(ErrorCode.WAITING_CANCELED);
    }
}
