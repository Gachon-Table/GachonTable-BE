package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.menu.domain.repository.MenuRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubRegisterRequest;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubDetailsResponse;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PubService {

    private static final Integer INITIAL_WAITING_COUNT = 0;

    private final PubRepository pubRepository;
    private final MenuRepository menuRepository;

    public List<GetPubsResponse> findAllPubs() {
        List<Pub> pubList = pubRepository.findAll();

        return pubList.stream().map(GetPubsResponse::of).toList();
    }

    public GetPubDetailsResponse findPubDetail(Integer pubId) {
        Pub pub = pubRepository.findById(pubId).orElseThrow(PubNotFoundException::new);
        List<Menu> menu = menuRepository.findAllByPub(pub);

        return GetPubDetailsResponse.of(pub, menu);
    }

    public RegisterResponse register(PubRegisterRequest request) {
        pubRepository.save(createPub(request));

        return new RegisterResponse(true, "주점 등록 성공");
    }

    public Pub createPub(PubRegisterRequest request) {
        return Pub.create(request.pubName(),
                request.oneLiner(),
                request.instagramUrl(),
                request.hours(),
                request.menuUrl(),
                request.openStatus(),
                request.waitingStatus(),
                INITIAL_WAITING_COUNT);
    }
}
