package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

import java.util.List;

public record PubManageRequest(List<String> thumbnails,
                               Boolean studentCard,
                               List<MenuRequest> menuRequests) {
    public record MenuRequest(String menuName,
                              String price,
                              String oneLiner) {
    }
}
