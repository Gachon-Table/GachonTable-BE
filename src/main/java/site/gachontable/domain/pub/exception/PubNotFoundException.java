package site.gachontable.domain.pub.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class PubNotFoundException extends ServiceException {
    public PubNotFoundException() {
        super(ErrorCode.PUB_NOT_FOUND);
    }
}
