package site.gachontable.gachontablebe.domain.user.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class EmptyQueueingCountException extends ServiceException {
    public EmptyQueueingCountException() {
        super(ErrorCode.EMPTY_QUEUEING_COUNT);
    }
}