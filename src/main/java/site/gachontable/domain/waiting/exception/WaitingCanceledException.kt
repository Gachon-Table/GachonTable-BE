package site.gachontable.domain.waiting.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class WaitingCanceledException : ServiceException(ErrorCode.WAITING_CANCELED)
