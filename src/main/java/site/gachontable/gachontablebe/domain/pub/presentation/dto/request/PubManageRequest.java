package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

import java.util.List;

public record PubManageRequest(String thumbnail,
                               Boolean studentCard,
                               List<MenuRequest> menuRequests) {
    record MenuRequest(String menuName,
                       String price,
                       String oneLiner) {
    }
}


