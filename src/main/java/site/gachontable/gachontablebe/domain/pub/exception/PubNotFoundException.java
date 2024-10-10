package site.gachontable.gachontablebe.domain.pub.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class PubNotFoundException extends ServiceException {
    public PubNotFoundException() {
        super(ErrorCode.PUB_NOT_FOUND);
    }
}
