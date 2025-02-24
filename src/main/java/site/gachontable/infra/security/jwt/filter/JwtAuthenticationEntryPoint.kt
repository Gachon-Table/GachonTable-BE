package site.gachontable.infra.security.jwt.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.independent.type.ErrorCode

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        setResponse(response)
    }

    fun setResponse(response: HttpServletResponse) {
        response.apply {
            contentType = "application/json;charset=UTF-8"
            status = HttpServletResponse.SC_UNAUTHORIZED
        }

        val mapper = ObjectMapper()
        val jsonResponse = mapper.writeValueAsString(
            ErrorResponse(ErrorCode.EMPTY_AUTHENTICATION)
        )

        response.writer.write(jsonResponse)
    }
}
