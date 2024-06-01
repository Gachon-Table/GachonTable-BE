package site.gachontable.gachontablebe.domain.pub.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.GetPubsDetailResponse;
import site.gachontable.gachontablebe.domain.pub.usecase.PubService;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.GetPubsResponse;
import site.gachontable.gachontablebe.global.error.ErrorResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PubController {
    private final PubService pubService;

    @Operation(summary = "주점 목록", description = "전체 주점 목록을 가져옵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/pubs")
    public ResponseEntity<List<GetPubsResponse>> getPubs() {
        return ResponseEntity.ok(pubService.findAllPubs());
    }

    @GetMapping("/pub/{pubId}")
    public ResponseEntity<GetPubsDetailResponse> getPubDetail(@PathVariable Integer pubId) {
        return ResponseEntity.ok(pubService.findPubDetail(pubId));
    }
}
