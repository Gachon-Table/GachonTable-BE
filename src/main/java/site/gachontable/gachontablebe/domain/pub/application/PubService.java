package site.gachontable.gachontablebe.domain.pub.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.dto.GetPubsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    public List<GetPubsResponse> findAllPubs() {
        List<Pub> pubList = pubRepository.findAll();

        return pubList.stream().map(GetPubsResponse::of).toList();
}
}
