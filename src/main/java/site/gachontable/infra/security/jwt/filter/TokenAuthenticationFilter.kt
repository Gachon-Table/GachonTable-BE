package site.gachontable.infra.security.jwt.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import site.gachontable.infra.security.jwt.JwtProvider

class TokenAuthenticationFilter(
    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token: String? = resolveToken(request)

        if (token != null && jwtProvider.isValidToken(token)) {
            val authentication = jwtProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val authorizationHeader: String? = request.getHeader(HEADER_AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substringAfter(TOKEN_PREFIX)
        }
        return null
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }
}
