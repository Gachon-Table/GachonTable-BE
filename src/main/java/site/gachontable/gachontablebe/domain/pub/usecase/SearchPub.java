package site.gachontable.gachontablebe.domain.pub.usecase;

import site.gachontable.gachontablebe.domain.pub.presentation.dto.response.GetPubsResponse;

import java.util.List;

public interface SearchPub {
    List<GetPubsResponse> execute(String keyword);
}
