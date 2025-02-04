package site.gachontable.domain.member.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class UserNotFoundException : ServiceException(ErrorCode.USER_NOT_FOUND)
