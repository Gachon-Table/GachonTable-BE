package site.gachontable.infra.biztalk.exception

import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class BiztalkSendFailException : ServiceException(ErrorCode.BIZTALK_SEND_FAIL)
