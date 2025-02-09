package site.gachontable.domain.pub.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class EmptyWaitingCountException : ServiceException(ErrorCode.EMPTY_WAITING_COUNT)
