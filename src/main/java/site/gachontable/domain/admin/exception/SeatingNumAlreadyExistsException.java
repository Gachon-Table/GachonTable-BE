package site.gachontable.domain.admin.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class SeatingNumAlreadyExistsException extends ServiceException {
    public SeatingNumAlreadyExistsException() {
        super(ErrorCode.SEATING_NUM_ALREADY_EXIST);
    }
}

