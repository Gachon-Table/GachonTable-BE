package site.gachontable.gachontablebe.domain.admin.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.*;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.AdminLoginResponse;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.WaitingInfosResponse;
import site.gachontable.gachontablebe.domain.admin.type.Status;
import site.gachontable.gachontablebe.domain.admin.usecase.*;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.PubManageRequest;
import site.gachontable.gachontablebe.domain.shared.dto.request.RefreshRequest;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.domain.waiting.usecase.GetWaitings;
import site.gachontable.gachontablebe.global.error.ErrorResponse;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminRegister adminRegister;
    private final AdminLogin adminLogin;
    private final PubRepository pubRepository;
    private final JwtProvider jwtProvider;
    private final GetWaitings getWaitings;
    private final EnterUser enterUser;
    private final CallUser callUser;
    private final ExitUser exitUser;
    private final ManagePub managePub;
    private final UpdateStatus updateStatus;
    private final GetSeatings getSeatings;

    @Operation(summary = "관리자 테스트 회원가입", description = "테스트를 위한 관리자 회원가입 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/test-register")
    public ResponseEntity<RegisterResponse> register(@RequestBody AdminRegisterRequest request) {
        Pub pub = pubRepository.findByPubId(request.pubId()).orElseThrow(PubNotFoundException::new);
        return ResponseEntity.ok(adminRegister.execute(request.username(), request.password(), request.tel(), pub));
    }

    @Operation(summary = "관리자 로그인", description = "관리자 계정으로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminLogin.execute(request.id(), request.password());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(관리자) 관리자 토큰 갱신", description = "관리자 계정의 액세스토큰을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(jwtProvider.refreshAccessToken(request.refreshToken()));
    }

    @Operation(summary = "웨이팅 대기열 조회", description = "관리자가 담당하는 주점의 대기열을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/waitings")
    public ResponseEntity<WaitingInfosResponse> getWaiting(@AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(getWaitings.execute(authDetails));
    }

    @Operation(summary = "주점 테이블 목록 조회", description = "관리자가 담당하는 주점의 테이블 사용 현황을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/seatings")
    public ResponseEntity<SeatingsResponse> getSeatings(@AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(getSeatings.execute(authDetails));
    }

    @Operation(summary = "사용자 입장완료", description = "관리자가 담당하는 주점의 사용자를 입장완료 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/enter")
    public ResponseEntity<String> enterUser(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody EnterUserRequest request) {
        return ResponseEntity.ok(enterUser.execute(authDetails, request, Status.ENTER.getStatusKo()));
    }

    @Operation(summary = "사용자 호출", description = "관리자가 담당하는 주점의 사용자를 호출합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/call")
    public ResponseEntity<String> callUser(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody CallUserRequest request) {
        return ResponseEntity.ok(callUser.execute(authDetails, request, Status.CALL.getStatusKo()));
    }

    @Operation(summary = "사용자 퇴장", description = "주점 이용을 완료한 사용자를 퇴장 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/exit")
    public ResponseEntity<String> exitUser(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody ExitUserRequest request) {
        return ResponseEntity.ok(exitUser.execute(authDetails, request));
    }

    @Operation(summary = "주점 영업 상태 변경", description = "관리자가 담당하는 주점의 상태(오픈 여부)를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/status")
    public ResponseEntity<RegisterResponse> updateOpenStatus(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(updateStatus.executeForOpenStatus(authDetails, request));
    }

    @Operation(summary = "주점 웨이팅 상태 변경", description = "관리자가 담당하는 주점의 상태(오픈 여부)를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/status-waiting")
    public ResponseEntity<RegisterResponse> updateWaitingStatus(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(updateStatus.executeForWaitingStatus(authDetails, request));
    }

    @Operation(summary = "주점 관리", description = "메뉴 등록, 대표 사진 및 학생증 필수 여부를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/manage")
    public ResponseEntity<String> managePub(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody PubManageRequest request){
        return ResponseEntity.ok(managePub.execute(authDetails, request));
    }
}
