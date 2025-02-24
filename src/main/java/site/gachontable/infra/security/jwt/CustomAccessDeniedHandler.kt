package site.gachontable.infra.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.independent.type.ErrorCode

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        setResponse(response)
    }

    fun setResponse(response: HttpServletResponse) {
        response.apply {
            contentType = "application/json;charset=UTF-8"
            status = HttpServletResponse.SC_FORBIDDEN
        }

        val mapper = ObjectMapper()
        val jsonResponse = mapper.writeValueAsString(
            ErrorResponse(ErrorCode.ROLE_FORBIDDEN)
        )

        response.writer.write(jsonResponse)
    }
}
