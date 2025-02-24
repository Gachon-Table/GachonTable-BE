package site.gachontable.infra.security.jwt.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class MalformedTokenException : ServiceException(ErrorCode.MALFORMED_TOKEN)
