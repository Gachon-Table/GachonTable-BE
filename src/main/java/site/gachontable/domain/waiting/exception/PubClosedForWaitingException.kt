package site.gachontable.domain.waiting.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class PubClosedForWaitingException extends ServiceException {
    public PubClosedForWaitingException() {
        super(ErrorCode.PUB_CLOSED_FOR_WAITING);
    }
}
