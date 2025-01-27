package site.gachontable.domain.pub.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class EmptyWaitingCountException extends ServiceException {
    public EmptyWaitingCountException() {
        super(ErrorCode.EMPTY_WAITING_COUNT);
    }
}
