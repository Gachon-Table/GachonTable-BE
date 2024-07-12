package site.gachontable.gachontablebe.domain.pub.presentation;

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
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubManageRequest;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubRegisterRequest;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubDetailsResponse;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;
import site.gachontable.gachontablebe.domain.pub.usecase.ManagePub;
import site.gachontable.gachontablebe.domain.pub.usecase.PubService;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/pub")
@RequiredArgsConstructor
public class PubController {
    private final PubService pubService;
    private final ManagePub managePub;

    @Operation(summary = "주점 목록", description = "전체 주점 목록을 가져옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<GetPubsResponse>> getAll() {
        return ResponseEntity.ok(pubService.findAllPubs());
    }

    @Operation(summary = "주점 상세정보", description = "주점의 상세정보를 가져옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{pubId}")
    public ResponseEntity<GetPubDetailsResponse> getPubDetail(@PathVariable Integer pubId) {
        return ResponseEntity.ok(pubService.findPubDetail(pubId));
    }

    @Operation(summary = "주점 등록", description = "주점을 새로 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody PubRegisterRequest request) {
        return ResponseEntity.ok(pubService.register(request));
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
