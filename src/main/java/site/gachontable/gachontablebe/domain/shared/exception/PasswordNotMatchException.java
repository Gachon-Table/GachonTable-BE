package site.gachontable.gachontablebe.domain.shared.exception;


import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class PasswordNotMatchException extends ServiceException {
    public PasswordNotMatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCH);
    }
}
