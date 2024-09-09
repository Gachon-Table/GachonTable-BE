package site.gachontable.gachontablebe.domain.admin.presentation.dto.request;

import java.util.List;

public record PubManageRequest(List<String> thumbnails,
                               List<MenuRequest> menuRequests) {
    public record MenuRequest(String thumbnail,
                              String menuName,
                              String price,
                              String oneLiner) {
    }
}
