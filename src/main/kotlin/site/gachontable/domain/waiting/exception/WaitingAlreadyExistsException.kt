package site.gachontable.domain.waiting.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class WaitingAlreadyExistsException : ServiceException(ErrorCode.WAITING_ALREADY_EXIST)
