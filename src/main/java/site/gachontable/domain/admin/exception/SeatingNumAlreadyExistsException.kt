package site.gachontable.domain.admin.exception

import site.gachontable.independent.type.ErrorCode
import site.gachontable.independent.error.exception.ServiceException

class SeatingNumAlreadyExistsException: ServiceException(ErrorCode.SEATING_NUM_ALREADY_EXIST)
