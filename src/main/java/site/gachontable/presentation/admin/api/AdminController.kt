package site.gachontable.presentation.admin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.gachontable.domain.admin.port.`in`.AdminLogin
import site.gachontable.domain.admin.port.`in`.AdminRegister
import site.gachontable.domain.admin.port.`in`.ExitUser
import site.gachontable.domain.admin.port.`in`.GetSeatings
import site.gachontable.domain.admin.port.`in`.ManagePub
import site.gachontable.domain.admin.port.`in`.UpdateStatus
import site.gachontable.domain.admin.service.CallUser
import site.gachontable.domain.admin.service.EnterUser
import site.gachontable.domain.admin.type.Status
import site.gachontable.domain.waiting.service.GetWaitings
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.infra.security.jwt.JwtProvider
import site.gachontable.infra.security.jwt.dto.JwtResponse
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.AdminLoginRequest
import site.gachontable.presentation.admin.dto.request.AdminRegisterRequest
import site.gachontable.presentation.admin.dto.request.CallUserRequest
import site.gachontable.presentation.admin.dto.request.EnterUserRequest
import site.gachontable.presentation.admin.dto.request.ExitUserRequest
import site.gachontable.presentation.admin.dto.request.PubManageRequest
import site.gachontable.presentation.admin.dto.request.UpdateStatusRequest
import site.gachontable.presentation.admin.dto.response.AdminLoginResponse
import site.gachontable.presentation.admin.dto.response.SeatingsResponse
import site.gachontable.presentation.admin.dto.response.WaitingInfosResponse
import site.gachontable.presentation.shared.dto.request.RefreshRequest
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminRegister: AdminRegister,
    private val adminLogin: AdminLogin,
    private val jwtProvider: JwtProvider,
    private val getWaitings: GetWaitings,
    private val enterUser: EnterUser,
    private val callUser: CallUser,
    private val exitUser: ExitUser,
    private val managePub: ManagePub,
    private val updateStatus: UpdateStatus,
    private val getSeatings: GetSeatings,
) {
    @Operation(summary = "관리자 테스트 회원가입", description = "테스트를 위한 관리자 회원가입 기능입니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/test-register")
    fun register(@RequestBody request: AdminRegisterRequest): ResponseEntity<RegisterResponse> {
        return ResponseEntity.ok<RegisterResponse>(adminRegister.execute(request))
    }

    @Operation(summary = "관리자 로그인", description = "관리자 계정으로 로그인합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/login")
    fun login(@RequestBody request: AdminLoginRequest): ResponseEntity<AdminLoginResponse> {
        val response: AdminLoginResponse = adminLogin.execute(request.id, request.password)
        return ResponseEntity.ok<AdminLoginResponse>(response)
    }

    @Operation(summary = "(관리자) 관리자 토큰 갱신", description = "관리자 계정의 액세스토큰을 갱신합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequest): ResponseEntity<JwtResponse> {
        return ResponseEntity.ok<JwtResponse>(jwtProvider.refreshAccessToken(request.refreshToken))
    }

    @Operation(summary = "웨이팅 대기열 조회", description = "관리자가 담당하는 주점의 대기열을 조회합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/waitings")
    fun getWaiting(@AuthenticationPrincipal authDetails: AuthDetails): ResponseEntity<WaitingInfosResponse> {
        return ResponseEntity.ok<WaitingInfosResponse>(getWaitings.execute(authDetails))
    }

    @Operation(summary = "주점 테이블 목록 조회", description = "관리자가 담당하는 주점의 테이블 사용 현황을 조회합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/seatings")
    fun getSeatings(@AuthenticationPrincipal authDetails: AuthDetails): ResponseEntity<SeatingsResponse> {
        return ResponseEntity.ok<SeatingsResponse>(getSeatings.execute(authDetails))
    }

    @Operation(summary = "사용자 입장완료", description = "관리자가 담당하는 주점의 사용자를 입장완료 처리합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/enter")
    fun enterUser(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: EnterUserRequest,
    ): ResponseEntity<String> {
        return ResponseEntity.ok<String>(enterUser.execute(authDetails, request, Status.ENTER.statusKo))
    }

    @Operation(summary = "사용자 호출", description = "관리자가 담당하는 주점의 사용자를 호출합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/call")
    fun callUser(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: CallUserRequest,
    ): ResponseEntity<String> {
        return ResponseEntity.ok<String>(callUser.execute(authDetails, request, Status.CALL.statusKo))
    }

    @Operation(summary = "사용자 퇴장", description = "주점 이용을 완료한 사용자를 퇴장 처리합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/exit")
    fun exitUser(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: ExitUserRequest,
    ): ResponseEntity<String> {
        return ResponseEntity.ok<String>(exitUser.execute(authDetails, request))
    }

    @Operation(summary = "주점 영업 상태 변경", description = "관리자가 담당하는 주점의 상태(오픈 여부)를 변경합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/status")
    fun updateOpenStatus(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: UpdateStatusRequest,
    ): ResponseEntity<RegisterResponse> {
        return ResponseEntity.ok<RegisterResponse>(updateStatus.executeForOpenStatus(authDetails, request))
    }

    @Operation(summary = "주점 웨이팅 상태 변경", description = "관리자가 담당하는 주점의 상태(오픈 여부)를 변경합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/status-waiting")
    fun updateWaitingStatus(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: UpdateStatusRequest,
    ): ResponseEntity<RegisterResponse> {
        return ResponseEntity.ok<RegisterResponse>(updateStatus.executeForWaitingStatus(authDetails, request))
    }

    @Operation(summary = "주점 관리", description = "메뉴 등록, 대표 사진 및 학생증 필수 여부를 수정할 수 있습니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/manage")
    fun managePub(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: PubManageRequest,
    ): ResponseEntity<String> {
        return ResponseEntity.ok<String>(managePub.execute(authDetails, request))
    }
}
