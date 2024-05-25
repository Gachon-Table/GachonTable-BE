package site.gachontable.gachontablebe.domain.user.Exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class EmptyQueingCountException extends ServiceException {
    public EmptyQueingCountException() {
        super(ErrorCode.EMPTY_QUEING_COUNT);
    }
}