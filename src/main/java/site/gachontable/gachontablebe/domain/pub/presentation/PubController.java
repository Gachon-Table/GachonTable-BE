package site.gachontable.gachontablebe.domain.pub.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubRegisterRequest;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubDetailsResponse;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;
import site.gachontable.gachontablebe.domain.pub.usecase.PubService;
import site.gachontable.gachontablebe.domain.pub.usecase.SearchPub;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/pub")
@RequiredArgsConstructor
public class PubController {

    private final PubService pubService;
    private final SearchPub searchPub;

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
    public ResponseEntity<GetPubDetailsResponse> getPubDetail(@PathVariable(value = "pubId") Integer pubId) {
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

    @Operation(summary = "주점 검색", description = "랜딩페이지에서 검색을 통해 원하는 주점을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<GetPubsResponse>> searchPub(@RequestParam(value = "keyword") String keyword) {
        return ResponseEntity.ok(searchPub.execute(keyword));
    }
}
