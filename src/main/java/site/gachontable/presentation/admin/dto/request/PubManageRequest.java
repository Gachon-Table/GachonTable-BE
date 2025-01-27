package site.gachontable.presentation.admin.dto.request;

import java.util.List;

public record PubManageRequest(List<String> thumbnails,
                               List<MenuRequest> menuRequests) {
    public record MenuRequest(Integer menuId,
                              String thumbnail,
                              String menuName,
                              String price,
                              String oneLiner) {
    }
}
