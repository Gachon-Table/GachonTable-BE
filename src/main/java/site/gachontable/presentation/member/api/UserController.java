package site.gachontable.presentation.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.gachontable.domain.shared.dto.request.RefreshRequest;
import site.gachontable.domain.shared.dto.request.TestRegisterRequest;
import site.gachontable.domain.shared.dto.response.RegisterResponse;
import site.gachontable.presentation.member.dto.request.UserLoginRequest;
import site.gachontable.domain.member.port.in.UserLogin;
import site.gachontable.domain.member.port.in.UserRegister;
import site.gachontable.independent.error.ErrorResponse;
import site.gachontable.infra.security.jwt.JwtProvider;
import site.gachontable.infra.security.jwt.dto.JwtResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRegister userRegister;
    private final UserLogin userLogin;
    private final JwtProvider jwtProvider;

    @Operation(summary = "유저 테스트 회원가입", description = "테스트를 위한 유저 회원가입 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/test-register")
    public ResponseEntity<RegisterResponse> register(@RequestBody TestRegisterRequest request) {
        return ResponseEntity.ok(userRegister.execute(request.username(), request.password(), request.tel()));
    }

    @Operation(summary = "유저 테스트 로그인", description = "테스트를 위한 유저 로그인 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/test-login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userLogin.execute(request.id(), request.password()));
    }

    @Operation(summary = "유저 토큰 갱신", description = "일반 사용자 계정의 액세스토큰을 갱신합니다.")
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
}
