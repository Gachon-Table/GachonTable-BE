package site.gachontable.independent.error.exception

import site.gachontable.independent.type.ErrorCode

open class ServiceException(val errorCode: ErrorCode) : RuntimeException()
