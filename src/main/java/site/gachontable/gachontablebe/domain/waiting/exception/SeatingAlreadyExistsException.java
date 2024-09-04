package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class SeatingAlreadyExistsException extends ServiceException {
    public SeatingAlreadyExistsException() {
        super(ErrorCode.SEATING_ALREADY_EXIST);
    }
}
