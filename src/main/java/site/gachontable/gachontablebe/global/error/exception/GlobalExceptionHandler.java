package site.gachontable.gachontablebe.global.error.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ErrorResponse handleException(ServiceException e){
        return new ErrorResponse(e.getErrorCode());
    }
}
