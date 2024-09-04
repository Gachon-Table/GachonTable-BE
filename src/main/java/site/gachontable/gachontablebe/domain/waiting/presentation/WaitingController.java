package site.gachontable.gachontablebe.domain.waiting.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.CancelRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.StatusResponse;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingHistoryResponse;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.type.Position;
import site.gachontable.gachontablebe.domain.waiting.usecase.*;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final CreateWaiting createWaiting;
    private final GetStatus getStatus;
    private final GetWaitingHistory getWaitingHistory;
    private final CancelWaiting cancelWaiting;
    private final GetStatusByBiztalk getStatusByBiztalk;

    @Operation(summary = "원격 웨이팅", description = "원격 웨이팅을 신규로 신청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/remote")
    public ResponseEntity<WaitingResponse> createRemote(@AuthenticationPrincipal AuthDetails authDetails,
                                                        @RequestBody RemoteWaitingRequest request) {
        return ResponseEntity.ok(createWaiting.execute(authDetails, request, Position.REMOTE.getPositionKo()));
    }

/*    @Operation(summary = "현장 웨이팅", description = "현장 웨이팅을 신규로 신청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/onsite")
    public ResponseEntity<WaitingResponse> createOnsite(@AuthenticationPrincipal AuthDetails authDetails,
                                                        @RequestBody OnsiteWaitingRequest request) {
        return ResponseEntity.ok(createWaiting.execute(authDetails, request, Position.ONSITE.getPositionKo()));
    }*/

    @Operation(summary = "웨이팅 현황 조회", description = "사용자(회원)가 자신의 신청한 웨이팅 현황을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/status")
    public ResponseEntity<List<StatusResponse>> getStatus(@AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(getStatus.execute(authDetails));
    }

    @Operation(summary = "웨이팅 내역 조회", description = "회원이 지금까지 신청한 웨이팅 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/history")
    public ResponseEntity<List<WaitingHistoryResponse>> getWaitingHistory(@AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(getWaitingHistory.execute(authDetails));
    }

    @Operation(summary = "웨이팅 취소", description = "회원이 개별 웨이팅을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/cancel")
    public ResponseEntity<WaitingResponse> cancel(@RequestBody CancelRequest request) {
        return ResponseEntity.ok(cancelWaiting.execute(request, Position.CANCEL.getPositionKo()));
    }

    @Operation(summary = "알림톡 웨이팅 현황 조회", description = "사용자(회원)가 자신의 신청한 웨이팅 현황을 알림톡을 통해 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/biztalk-status/{waitingId}")
    public ResponseEntity<StatusResponse> getStatusByBiztalk(@PathVariable(value = "waitingId") UUID waitingId) {
        return ResponseEntity.ok(getStatusByBiztalk.execute(waitingId));
    }
}
