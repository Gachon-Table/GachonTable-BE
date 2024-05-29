package site.gachontable.gachontablebe.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.gachontable.gachontablebe.domain.shared.dto.request.TestRegisterRequest;
import site.gachontable.gachontablebe.domain.shared.dto.response.TestRegisterResponse;
import site.gachontable.gachontablebe.domain.user.usecase.UserRegister;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRegister userRegister;

    @Operation(summary = "유저 테스트 회원가입", description = "테스트를 위한 유저 회원가입 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<TestRegisterResponse> register(@RequestBody TestRegisterRequest request) {
        return ResponseEntity.ok(userRegister.execute(request.username(), request.password(), request.tel()));
    }
}
