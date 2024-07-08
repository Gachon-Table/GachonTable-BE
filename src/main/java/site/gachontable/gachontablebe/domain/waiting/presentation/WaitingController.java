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
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.OnsiteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.RemoteWaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.OrderResponse;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingHistoryResponse;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.usecase.CancelWaiting;
import site.gachontable.gachontablebe.domain.waiting.usecase.CreateWaiting;
import site.gachontable.gachontablebe.domain.waiting.usecase.GetOrder;
import site.gachontable.gachontablebe.domain.waiting.usecase.GetWaitingHistory;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final CreateWaiting createWaiting;
    private final GetOrder getOrder;
    private final GetWaitingHistory getWaitingHistory;
    private final CancelWaiting cancelWaiting;

    @Operation(summary = "원격 웨이팅", description = "원격 웨이팅을 신규로 생성합니다.")
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
        return ResponseEntity.ok(createWaiting.execute(authDetails, request));
    }

    @Operation(summary = "현장 웨이팅", description = "현장 웨이팅을 신규로 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/onsite")
    public ResponseEntity<WaitingResponse> createOnsite(@RequestBody OnsiteWaitingRequest request) {
        return ResponseEntity.ok(createWaiting.execute(request));
    }

    @Operation(summary = "순번 조회", description = "사용자(회원)가 자신의 신청한 웨이팅 순번 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/order")
    public ResponseEntity<List<OrderResponse>> getOrder(@AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(getOrder.execute(authDetails));
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
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/cancel")
    public ResponseEntity<WaitingResponse> cancel(@RequestBody CancelRequest request) {
        return ResponseEntity.ok(cancelWaiting.execute(request));
    }
}
