package site.gachontable.domain.pub.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class PubMismatchException extends ServiceException {
    public PubMismatchException() {
        super(ErrorCode.PUB_MISMATCH);
    }
}
