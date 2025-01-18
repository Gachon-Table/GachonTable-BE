package site.gachontable.domain.waiting.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class WaitingAlreadyExistsException extends ServiceException {
    public WaitingAlreadyExistsException() {
        super(ErrorCode.WAITING_ALREADY_EXIST);
    }
}
