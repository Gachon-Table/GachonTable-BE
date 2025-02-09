package site.gachontable.domain.pub.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class PubNotOpenException : ServiceException(ErrorCode.PUB_NOT_OPEN)
