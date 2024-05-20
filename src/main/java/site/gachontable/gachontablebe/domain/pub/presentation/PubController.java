package site.gachontable.gachontablebe.domain.pub.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.gachontable.gachontablebe.domain.pub.application.PubService;
import site.gachontable.gachontablebe.domain.pub.dto.GetPubsResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PubController {
    private final PubService pubService;

    @GetMapping("/pubs")
    public ResponseEntity<List<GetPubsResponseDto>> getPubs() {
        return ResponseEntity.ok(pubService.getPubs());
    }
}
