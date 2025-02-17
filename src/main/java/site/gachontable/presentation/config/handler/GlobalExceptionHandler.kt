package site.gachontable.presentation.config.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(e.errorCode)
        return ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(e.errorCode.httpStatus))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        val errorResponse = ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}