package site.gachontable.gachontablebe.domain.waiting.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.request.WaitingRequest;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.WaitingResponse;
import site.gachontable.gachontablebe.domain.waiting.usecase.CreateWaitingRemoteImpl;
import site.gachontable.gachontablebe.global.error.ErrorResponse;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final CreateWaitingRemoteImpl createWaitingRemote;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Operation(summary = "원격 웨이팅", description = "원격 웨이팅을 신규로 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/remote/{pubId}")
    public ResponseEntity<WaitingResponse> createOnsite(@RequestHeader("Authorization") String authorizationHeader,
                                                        @PathVariable Integer pubId,
                                                        @RequestBody WaitingRequest request) {
        User user = userRepository.findById(jwtProvider.getUserIdFromToken(authorizationHeader))
                .orElseThrow(UserNotFoundException::new);
        createWaitingRemote.execute(user, pubId, request);
        return ResponseEntity.ok(null);
    }
}
