package site.gachontable.domain.waiting.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class UserWaitingLimitExcessException extends ServiceException {
    public UserWaitingLimitExcessException() {
        super(ErrorCode.WAITING_OVER_COUNT);
    }
}
