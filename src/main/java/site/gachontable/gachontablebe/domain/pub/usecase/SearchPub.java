package site.gachontable.gachontablebe.domain.pub.usecase;

import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.PubSearchResponse;

public interface SearchPub {
    PubSearchResponse execute(String keyword);
}
