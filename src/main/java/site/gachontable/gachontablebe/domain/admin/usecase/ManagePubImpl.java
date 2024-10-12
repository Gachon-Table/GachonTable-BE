package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.menu.domain.repository.MenuRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.Thumbnail;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.PubManageRequest;
import site.gachontable.gachontablebe.domain.pub.domain.repository.ThumbnailRepository;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ManagePubImpl implements ManagePub {

    private final AdminRepository adminRepository;
    private final MenuRepository menuRepository;
    private final ThumbnailRepository thumbnailRepository;

    @Override
    @Transactional
    public String execute(AuthDetails authDetails, PubManageRequest request) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        manageThumbnails(request.thumbnails(), pub);
        manageMenus(request, pub);

        return SuccessCode.MANAGE_PUB_SUCCESS.getMessage();
    }

    private void manageThumbnails(List<String> thumbnails, Pub pub) {
        List<Thumbnail> existingThumbnails = thumbnailRepository.findAllByPub(pub);

        IntStream.range(0, thumbnails.size()).forEach(i -> {
            String url = thumbnails.get(i);

            if (i < existingThumbnails.size()) {
                Thumbnail existingThumbnail = existingThumbnails.get(i);
                if (!existingThumbnail.getUrl().equals(url)) {
                    existingThumbnail.update(url);
                }

                return;
            }
            thumbnailRepository.save(Thumbnail.create(url, pub));
        });
    }

    private void manageMenus(PubManageRequest request, Pub pub) {
        Map<Integer, Menu> existingMenus = menuRepository.findAllByPub(pub).stream()
                .collect(Collectors.toMap(Menu::getMenuId, menu -> menu));

        List<Menu> updatedMenus = request.menuRequests().stream()
                .map(menuRequest -> {
                    Menu menu = existingMenus.get(menuRequest.menuId());
                    if (menu != null) {
                        menu.update(
                                menuRequest.menuName(),
                                menuRequest.price(),
                                menuRequest.oneLiner(),
                                menuRequest.thumbnail());
                        return menu;
                    }
                    return Menu.create(
                            pub,
                            menuRequest.menuName(),
                            menuRequest.price(),
                            menuRequest.oneLiner(),
                            menuRequest.thumbnail());
                }).toList();

        menuRepository.saveAll(updatedMenus);
    }
}
