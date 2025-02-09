package site.gachontable.presentation.pub.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.gachontable.domain.pub.service.PubService
import site.gachontable.independent.error.ErrorResponse
import site.gachontable.presentation.pub.dto.request.PubRegisterRequest
import site.gachontable.presentation.pub.dto.response.GetPubDetailsResponse
import site.gachontable.presentation.pub.dto.response.GetPubsResponse
import site.gachontable.presentation.shared.dto.response.RegisterResponse

@RestController
@RequestMapping("/pub")
class PubController(
    private val pubService: PubService,
) {
    @Operation(summary = "주점 목록", description = "전체 주점 목록을 가져옵니다")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/all")
    fun getAll(): ResponseEntity<MutableList<GetPubsResponse>> {
        return ResponseEntity.ok<MutableList<GetPubsResponse>>(pubService.findAllPubs())
    }

    @Operation(summary = "주점 상세정보", description = "주점의 상세정보를 가져옵니다")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @GetMapping("/{pubId}")
    fun getPubDetail(@PathVariable(value = "pubId") pubId: Int): ResponseEntity<GetPubDetailsResponse> {
        return ResponseEntity.ok<GetPubDetailsResponse>(pubService.findPubDetail(pubId))
    }

    @Operation(summary = "주점 등록", description = "주점을 새로 등록합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "201"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "403", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    )
    @PostMapping("/register")
    fun register(@RequestBody request: PubRegisterRequest): ResponseEntity<RegisterResponse> {
        return ResponseEntity.ok<RegisterResponse>(pubService.register(request))
    }
}
