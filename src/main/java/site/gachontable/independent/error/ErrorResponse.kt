package site.gachontable.independent.error

import site.gachontable.independent.type.ErrorCode

class ErrorResponse(errorCode: ErrorCode) {
    val httpStatus = errorCode.httpStatus
    val message = errorCode.message
    val code = errorCode.code

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode)
        }
    }
}
