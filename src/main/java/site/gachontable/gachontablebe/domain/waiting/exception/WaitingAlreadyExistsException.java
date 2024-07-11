package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class WaitingAlreadyExistsException extends ServiceException {
    public WaitingAlreadyExistsException() {
        super(ErrorCode.WAITING_ALREADY_EXIST);
    }
}