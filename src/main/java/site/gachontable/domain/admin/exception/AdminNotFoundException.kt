package site.gachontable.domain.admin.exception

import site.gachontable.independent.type.ErrorCode
import site.gachontable.independent.error.exception.ServiceException

class AdminNotFoundException: ServiceException(ErrorCode.ADMIN_NOT_FOUND)
