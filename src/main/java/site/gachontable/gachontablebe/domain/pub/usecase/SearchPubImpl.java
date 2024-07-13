package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.PubSearchResponse;

@Service
@RequiredArgsConstructor
public class SearchPubImpl implements SearchPub {
    @Override
    public PubSearchResponse execute(String keyword) {
        return null;
    }
}
