package site.gachontable.gachontablebe.domain.pub.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class PubMismatchException extends ServiceException {
    public PubMismatchException() {
        super(ErrorCode.PUB_MISMATCH);
    }
}