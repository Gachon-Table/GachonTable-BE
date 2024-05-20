package site.gachontable.gachontablebe.domain.pub.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.dto.GetPubsResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    public List<GetPubsResponseDto> getPubs() {
        List<Pub> pubList = pubRepository.findAll();

        return pubList.stream().map(pub ->
                GetPubsResponseDto.builder()
                        .pubId(pub.getPubId())
                        .url(pub.getPubThumbnail())
                        .pubName(pub.getPubName())
                        .oneLiner(pub.getOneLiner())
                        .studentCard(pub.getStudentCard())
                        .menu(pub.getRepresentativeMenu())
                        .queueing(pub.getWaitingQueue().size())
                        .build()
        ).toList();
}
}
