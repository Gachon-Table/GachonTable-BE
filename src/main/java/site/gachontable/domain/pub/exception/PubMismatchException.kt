package site.gachontable.domain.pub.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class PubMismatchException : ServiceException(ErrorCode.PUB_MISMATCH)
