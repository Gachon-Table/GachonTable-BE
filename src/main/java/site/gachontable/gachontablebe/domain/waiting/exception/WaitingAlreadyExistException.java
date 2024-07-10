package site.gachontable.gachontablebe.domain.waiting.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class WaitingAlreadyExistException extends ServiceException {
    public WaitingAlreadyExistException() {
        super(ErrorCode.WAITING_ALREADY_EXIST);
    }
}