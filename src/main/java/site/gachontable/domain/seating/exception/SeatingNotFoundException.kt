package site.gachontable.domain.seating.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class SeatingNotFoundException : ServiceException(ErrorCode.SEATING_NOT_FOUND)
