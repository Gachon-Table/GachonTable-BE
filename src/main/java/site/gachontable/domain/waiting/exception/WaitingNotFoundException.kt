package site.gachontable.domain.waiting.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class WaitingNotFoundException : ServiceException(ErrorCode.WAITING_NOT_FOUND)
