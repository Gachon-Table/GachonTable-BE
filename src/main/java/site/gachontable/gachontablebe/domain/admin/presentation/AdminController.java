package site.gachontable.gachontablebe.domain.admin.presentation;

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
import site.gachontable.gachontablebe.domain.admin.dto.AdminLoginRequest;
import site.gachontable.gachontablebe.domain.admin.dto.ForceCancelRequest;
import site.gachontable.gachontablebe.domain.admin.usecase.AdminLogin;
import site.gachontable.gachontablebe.domain.admin.usecase.ForceCancel;
import site.gachontable.gachontablebe.global.error.ErrorResponse;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminLogin adminLogin;
    private final ForceCancel forceCancel;

    @Operation(summary = "관리자 로그인", description = "관리자 계정으로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AdminLoginRequest request) {
        JwtResponse tokens = adminLogin.execute(request.id(), request.password());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/force-cancel")
    public ResponseEntity<String> forceCancel(@RequestBody ForceCancelRequest request) {
        return ResponseEntity.ok(forceCancel.cancel(request.userId()));
    }
}
