package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.RegisterRequest;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubDetailsResponse;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    public List<GetPubsResponse> findAllPubs() {
        List<Pub> pubList = pubRepository.findAll();

        return pubList.stream().map(GetPubsResponse::of).toList();
    }

    public GetPubDetailsResponse findPubDetail(Integer pubId) {
        Pub pub = pubRepository.findByPubId(pubId).orElseThrow(PubNotFoundException::new);
        List<Menu> menu = pub.getMenus();
        return GetPubDetailsResponse.of(pub, menu);
    }

    public RegisterResponse register(RegisterRequest request) {
        pubRepository.save(createPub(request));
        return new RegisterResponse(true, "주점 등록 성공");
    }

    public Pub createPub(RegisterRequest request) {
        List<Menu> menus = new ArrayList<>();
        Queue<Waiting> waitings = new LinkedList<>();

        return Pub.create(request.pubName(),
                request.oneLiner(),
                request.pubTel(),
                request.studentCard(),
                request.representativeMenu(),
                request.pubLoc(),
                request.pubThumbnail(),
                menus,
                request.openStatus(),
                waitings);
    }

}
