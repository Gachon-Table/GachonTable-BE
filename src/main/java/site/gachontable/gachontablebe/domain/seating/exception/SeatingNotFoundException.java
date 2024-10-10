package site.gachontable.gachontablebe.domain.seating.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class SeatingNotFoundException extends ServiceException {
    public SeatingNotFoundException() {
        super(ErrorCode.SEATING_NOT_FOUND);
    }
}
