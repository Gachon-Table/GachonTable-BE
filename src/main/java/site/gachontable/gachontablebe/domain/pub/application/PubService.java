package site.gachontable.gachontablebe.domain.pub.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.dto.GetPubsResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    public void getPubs() {
        List<Pub> pubList = pubRepository.findAll();
    }
}
