package site.gachontable.gachontablebe.domain.pub.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class EmptyWaitingCountException extends ServiceException {
    public EmptyWaitingCountException() {
        super(ErrorCode.EMPTY_WAITING_COUNT);
    }
}
