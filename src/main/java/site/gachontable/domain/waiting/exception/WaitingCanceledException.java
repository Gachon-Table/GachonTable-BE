package site.gachontable.domain.waiting.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class WaitingCanceledException extends ServiceException {
    public WaitingCanceledException() {
        super(ErrorCode.WAITING_CANCELED);
    }
}
