package site.gachontable.gachontablebe.global.biztalk.exception;

import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

public class BiztalkSendFailException extends ServiceException {
    public BiztalkSendFailException() {
        super(ErrorCode.BIZTALK_SEND_FAIL);
    }
}
