package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPubImpl implements SearchPub {
    private final PubRepository pubRepository;

    @Override
    public List<GetPubsResponse> execute(String keyword) {
        List<Pub> pubs = pubRepository.findAllByTitleOrTextDataContaining(keyword);

        return pubs.stream().map(GetPubsResponse::of).toList();
    }
}
