package site.gachontable.presentation.auth.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import site.gachontable.domain.auth.service.AuthService
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.presentation.auth.dto.response.AuthResponse

@RestController
class AuthController(
    private val authService: AuthService,
) {
    @Operation(summary = "카카오 로그인", description = "토큰을 통해 사용자를 로그인합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/login")
    fun login(@RequestParam(value = "code") code: String): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok<AuthResponse>(authService.getUserInfo(code))
    }

    @GetMapping("/health-check")
    fun checkHealthStatus(): ResponseEntity<Void> {
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}
