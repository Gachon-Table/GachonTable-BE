package site.gachontable.gachontablebe.domain.pub.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.gachontable.gachontablebe.domain.pub.dto.GetPubsResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PubController {

    //ResponseEntity<List<GetPubsResponseDto>>
    @GetMapping("/pubs")
    public void getPubs() {

    }
}
