package site.gachontable.gachontablebe.domain.pub.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class PubNotOpenException extends ServiceException {
    public PubNotOpenException() {
        super(ErrorCode.PUB_NOT_OPEN);
    }
}
