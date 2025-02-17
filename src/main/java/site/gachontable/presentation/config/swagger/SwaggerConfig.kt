package site.gachontable.presentation.config.swagger

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "라인업지 API", description = "라인업지 API 문서", version = "v1.0.0"
    )
)
@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI {
        val jwt = "JWT"
        val securityRequirement = SecurityRequirement().addList(jwt)
        val components = Components().addSecuritySchemes(
            jwt, SecurityScheme().apply {
                name(jwt)
                type(SecurityScheme.Type.HTTP)
                scheme("bearer")
                bearerFormat(jwt)
            }
        )

        val apiServer = Server().apply {
            url = "https://api.lupg.me"
        }

        val server = Server().apply {
            url = "/"
        }

        return OpenAPI().apply {
            servers(listOf<Server>(apiServer, server))
            addSecurityItem(securityRequirement)
            components(components)
        }
    }
}
