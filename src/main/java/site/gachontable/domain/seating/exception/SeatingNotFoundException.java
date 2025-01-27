package site.gachontable.domain.seating.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class SeatingNotFoundException extends ServiceException {
    public SeatingNotFoundException() {
        super(ErrorCode.SEATING_NOT_FOUND);
    }
}
