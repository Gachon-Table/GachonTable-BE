package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.GetPubsDetailResponse;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.GetPubsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    public List<GetPubsResponse> findAllPubs() {
        List<Pub> pubList = pubRepository.findAll();

        return pubList.stream().map(GetPubsResponse::of).toList();
}
    public GetPubsDetailResponse findPubDetail(Integer pubId) {
        Pub pub = pubRepository.findByPubId(pubId).orElseThrow(PubNotFoundException::new);
        List<Menu> menu = pub.getMenus();
        return GetPubsDetailResponse.from(pub,menu);
    }
}
