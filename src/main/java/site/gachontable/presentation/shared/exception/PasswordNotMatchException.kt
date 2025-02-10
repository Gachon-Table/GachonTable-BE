package site.gachontable.presentation.shared.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class PasswordNotMatchException : ServiceException(ErrorCode.PASSWORD_NOT_MATCH)
