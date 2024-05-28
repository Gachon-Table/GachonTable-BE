package site.gachontable.gachontablebe.domain.user.Exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}