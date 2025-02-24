package site.gachontable.presentation.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.gachontable.domain.member.port.`in`.UserLogin
import site.gachontable.domain.member.port.`in`.UserRegister
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.infra.security.jwt.dto.JwtResponse
import site.gachontable.presentation.member.dto.request.UserLoginRequest
import site.gachontable.presentation.shared.dto.request.RefreshRequest
import site.gachontable.presentation.shared.dto.request.TestRegisterRequest
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@RestController
@RequestMapping("/user")
class UserController(
    private val userRegister: UserRegister,
    private val userLogin: UserLogin,
    private val jwtProvider: JwtProvider,
) {
    @Operation(summary = "유저 테스트 회원가입", description = "테스트를 위한 유저 회원가입 기능입니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/test-register")
    fun register(@RequestBody request: TestRegisterRequest): ResponseEntity<RegisterResponse> {
        return ResponseEntity.ok<RegisterResponse>(
            userRegister.execute(
                request.username,
                request.password,
                request.tel
            )
        )
    }

    @Operation(summary = "유저 테스트 로그인", description = "테스트를 위한 유저 로그인 기능입니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/test-login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<JwtResponse> {
        return ResponseEntity.ok<JwtResponse>(userLogin.execute(request.id, request.password))
    }

    @Operation(summary = "유저 토큰 갱신", description = "일반 사용자 계정의 액세스토큰을 갱신합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "401", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequest): ResponseEntity<JwtResponse> {
        return ResponseEntity.ok<JwtResponse>(jwtProvider.refreshAccessToken(request.refreshToken))
    }
}
