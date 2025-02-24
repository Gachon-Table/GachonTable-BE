package site.gachontable.infra.security.jwt.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode

class ExceptionHandleFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        runCatching {
            filterChain.doFilter(request, response)
        }.onFailure { exception ->
            when (exception) {
                is ServiceException -> sendErrorResponse(response, exception.errorCode)
                else -> {
                    exception.printStackTrace()
                    sendErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }

    private fun sendErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.apply {
            status = errorCode.httpStatus
            characterEncoding = "UTF-8"
            contentType = "application/json"
        }
        val objectMapper = ObjectMapper()
        val errorResponse: ErrorResponse = ErrorResponse.of(errorCode)
        val result = mapOf("result" to errorResponse)

        objectMapper.writeValue(response.writer, result)
    }
}
