package site.gachontable.gachontablebe.global.error.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ErrorResponse handleException(ServiceException e){
        return new ErrorResponse(e.getErrorCode());
    }
}
