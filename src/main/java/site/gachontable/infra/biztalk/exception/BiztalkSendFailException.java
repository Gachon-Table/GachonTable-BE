package site.gachontable.infra.biztalk.exception;

import site.gachontable.independent.type.ErrorCode;
import site.gachontable.independent.error.exception.ServiceException;

public class BiztalkSendFailException extends ServiceException {
    public BiztalkSendFailException() {
        super(ErrorCode.BIZTALK_SEND_FAIL);
    }
}
