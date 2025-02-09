package site.gachontable.domain.pub.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class PubNotOpenException extends ServiceException {
    public PubNotOpenException() {
        super(ErrorCode.PUB_NOT_OPEN);
    }
}
