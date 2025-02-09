package site.gachontable.presentation.waiting.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.gachontable.domain.waiting.port.`in`.CancelWaiting
import site.gachontable.domain.waiting.port.`in`.CreateWaiting
import site.gachontable.domain.waiting.port.`in`.GetStatus
import site.gachontable.domain.waiting.port.`in`.GetStatusByBiztalk
import site.gachontable.domain.waiting.service.GetWaitingHistory
import site.gachontable.domain.waiting.type.Position
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.waiting.dto.request.CancelRequest
import site.gachontable.presentation.waiting.dto.request.RemoteWaitingRequest
import site.gachontable.presentation.waiting.dto.response.StatusResponse
import site.gachontable.presentation.waiting.dto.response.WaitingHistoryResponse
import site.gachontable.presentation.waiting.dto.response.WaitingResponse
import java.util.*

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
class WaitingController(
    private val createWaiting: CreateWaiting,
    private val getStatus: GetStatus,
    private val getWaitingHistory: GetWaitingHistory,
    private val cancelWaiting: CancelWaiting,
    private val getStatusByBiztalk: GetStatusByBiztalk,
) {
    @Operation(summary = "원격 웨이팅", description = "원격 웨이팅을 신규로 신청합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/remote")
    fun createRemote(
        @AuthenticationPrincipal authDetails: AuthDetails,
        @RequestBody request: RemoteWaitingRequest,
    ): ResponseEntity<WaitingResponse> {
        return ResponseEntity.ok<WaitingResponse>(
            createWaiting.execute(
                authDetails,
                request,
                Position.REMOTE.positionKo
            )
        )
    }

    @Operation(summary = "웨이팅 현황 조회", description = "사용자(회원)가 자신의 신청한 웨이팅 현황을 조회합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/status")
    fun getStatus(@AuthenticationPrincipal authDetails: AuthDetails): ResponseEntity<MutableList<StatusResponse>> {
        return ResponseEntity.ok<MutableList<StatusResponse>>(getStatus.execute(authDetails))
    }

    @Operation(summary = "웨이팅 내역 조회", description = "회원이 지금까지 신청한 웨이팅 내역을 조회합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/history")
    fun getWaitingHistory(@AuthenticationPrincipal authDetails: AuthDetails): ResponseEntity<MutableList<WaitingHistoryResponse>> {
        return ResponseEntity.ok<MutableList<WaitingHistoryResponse>>(getWaitingHistory.execute(authDetails))
    }

    @Operation(summary = "웨이팅 취소", description = "회원이 개별 웨이팅을 취소합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PatchMapping("/cancel")
    fun cancel(@RequestBody request: CancelRequest): ResponseEntity<WaitingResponse> {
        return ResponseEntity.ok<WaitingResponse>(cancelWaiting.execute(request, Position.CANCEL.positionKo))
    }

    @Operation(summary = "알림톡 웨이팅 현황 조회", description = "사용자(회원)가 자신의 신청한 웨이팅 현황을 알림톡을 통해 조회합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/biztalk-status/{waitingId}")
    fun getStatusByBiztalk(@PathVariable(value = "waitingId") waitingId: UUID): ResponseEntity<StatusResponse> {
        return ResponseEntity.ok<StatusResponse>(getStatusByBiztalk.execute(waitingId))
    }
}
