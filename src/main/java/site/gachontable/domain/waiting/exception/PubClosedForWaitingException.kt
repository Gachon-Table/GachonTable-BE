package site.gachontable.domain.waiting.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class PubClosedForWaitingException : ServiceException(ErrorCode.PUB_CLOSED_FOR_WAITING)
