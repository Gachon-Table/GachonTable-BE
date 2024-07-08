package site.gachontable.gachontablebe.domain.admin.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.AdminLoginRequest;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.AdminRegisterRequest;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.EnterUserRequest;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.AdminLoginResponse;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.EnterUserResponse;
import site.gachontable.gachontablebe.domain.admin.usecase.AdminLogin;
import site.gachontable.gachontablebe.domain.admin.usecase.AdminRegister;
import site.gachontable.gachontablebe.domain.admin.usecase.EnterUser;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.PubWaitingListResponse;
import site.gachontable.gachontablebe.domain.waiting.usecase.GetWaitings;
import site.gachontable.gachontablebe.global.error.ErrorResponse;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminRegister adminRegister;
    private final AdminLogin adminLogin;
    private final PubRepository pubRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final GetWaitings getWaitings;
    private final EnterUser enterUser;

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

    @Operation(summary = "웨이팅 대기열 조회", description = "관리자가 담당하는 주점의 대기열을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/waitings")
    public ResponseEntity<PubWaitingListResponse> getWaiting(@RequestHeader("Authorization") String authorizationHeader) {
        Pub pub = adminRepository.findById(jwtProvider.getUserIdFromToken(authorizationHeader))
                .orElseThrow(AdminNotFoundException::new)
                .getPub();
        return ResponseEntity.ok(getWaitings.excute(pub));
    }

    @Operation(summary = "사용자 입장완료", description = "관리자가 담당하는 주점의 사용자를 입장완료 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/entered")
    public ResponseEntity<EnterUserResponse> enterUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EnterUserRequest request) {
        Admin admin = adminRepository.findById(jwtProvider.getUserIdFromToken(authorizationHeader))
                .orElseThrow(AdminNotFoundException::new);
        return ResponseEntity.ok(enterUser.execute(admin, request.waitingId()));
    }
}
