package site.gachontable.infra.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import site.gachontable.infra.security.jwt.CustomAccessDeniedHandler
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.infra.security.jwt.filter.ExceptionHandleFilter
import site.gachontable.infra.security.jwt.filter.JwtAuthenticationEntryPoint
import site.gachontable.infra.security.jwt.filter.TokenAuthenticationFilter
import site.gachontable.presentation.shared.Role

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: JwtProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,

    @Value("\${management.endpoints.web.base-path}")
    private val actuatorBasePath: String,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val whitePaths = arrayOf(
            "/swagger-ui/**",
            "/v3/**",
            "/health-check",
            "$actuatorBasePath/**",
            "/",
            "/login",
            "/admin/test-register",
            "/admin/login",
            "/user/test-register",
            "/user/test-login",
            "/user/refresh",
            "/admin/refresh",
            "/pub/register",
            "/pub/all",
            "/pub/{pubId}",
            "/waiting/cancel",
            "/waiting/biztalk-status/{waitingId}"
        )

        val adminPaths = arrayOf(
            "/admin/waitings",
            "/admin/seatings",
            "/admin/enter",
            "/admin/call",
            "/admin/exit",
            "/admin/status",
            "/admin/status-waiting",
            "/admin/manage",
        )

        val waitingPaths = arrayOf(
            "/waiting/remote",
            "/waiting/status",
            "/waiting/history",
        )

        http {
            authorizeHttpRequests {
                whitePaths.forEach { authorize(it, permitAll) }
                adminPaths.forEach { authorize(it, hasRole(Role.ROLE_ADMIN.role)) }
                waitingPaths.forEach { authorize(it, hasRole(Role.ROLE_USER.role)) }
                authorize(anyRequest, authenticated)
            }
        }

        http {
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            cors {
                configurationSource = corsConfigurationSource()
            }
        }

        http {
            exceptionHandling {
                accessDeniedHandler = customAccessDeniedHandler
                authenticationEntryPoint = jwtAuthenticationEntryPoint
            }
        }

        http {
            oauth2Login { }
        }

        // Ensure ExceptionHandleFilter comes before TokenAuthenticationFilter,
        // and then TokenAuthenticationFilter is placed before the UsernamePasswordAuthenticationFilter.
        http {
            addFilterBefore<TokenAuthenticationFilter>(
                filter = ExceptionHandleFilter()
            )
            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                filter = TokenAuthenticationFilter(tokenProvider)
            )
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf(
                "http://localhost:8080",
                "http://localhost:3000",
                "https://api.lupg.me",
                "https://lupg.me",
                "https://www.lupg.me",
                "https://test.lupg.me"
            )
            allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowCredentials = true
            allowedHeaders = listOf("*")
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}
