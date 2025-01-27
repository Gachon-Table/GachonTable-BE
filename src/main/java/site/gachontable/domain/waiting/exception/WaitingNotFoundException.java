package site.gachontable.domain.waiting.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class WaitingNotFoundException extends ServiceException {
    public WaitingNotFoundException() {
        super(ErrorCode.WAITING_NOT_FOUND);
    }
}
