package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class PubClosedForWaitingException extends ServiceException {
    public PubClosedForWaitingException() {
        super(ErrorCode.PUB_CLOSED_FOR_WAITING);
    }
}
