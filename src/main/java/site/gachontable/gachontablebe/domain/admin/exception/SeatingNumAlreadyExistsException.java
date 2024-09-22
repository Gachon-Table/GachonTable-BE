package site.gachontable.gachontablebe.domain.admin.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class SeatingNumAlreadyExistsException extends ServiceException {
    public SeatingNumAlreadyExistsException() {
        super(ErrorCode.SEATING_NUM_ALREADY_EXIST);
    }
}

