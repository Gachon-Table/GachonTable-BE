package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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

@Service
@RequiredArgsConstructor
public class ManagePubImpl implements ManagePub {

    private final AdminRepository adminRepository;
    private final MenuRepository menuRepository;
    private final ThumbnailRepository thumbnailRepository;

    @Caching(
            evict = {
                    @CacheEvict(value = "menuCache", cacheManager = "menuCacheManager", allEntries = true),
                    @CacheEvict(value = "thumbnailsCache", cacheManager = "thumbnailsCacheManager", allEntries = true)
            }
    )
    @Transactional
    @Override
    public String execute(AuthDetails authDetails, PubManageRequest request) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        replaceThumbnails(request.thumbnails(), pub);
        replaceMenus(request.menuRequests(), pub);

        return SuccessCode.MANAGE_PUB_SUCCESS.getMessage();
    }

    private void replaceThumbnails(List<String> thumbnails, Pub pub) {
        thumbnailRepository.deleteAllByPub(pub);

        List<Thumbnail> newThumbnails = thumbnails.stream()
                .map(url -> Thumbnail.create(url, pub))
                .toList();

        thumbnailRepository.saveAll(newThumbnails);
    }

    private void replaceMenus(List<PubManageRequest.MenuRequest> request, Pub pub) {
        menuRepository.deleteAllByPub(pub);

        List<Menu> updatedMenus = request.stream()
                .map(menuRequest -> Menu.create(
                        pub,
                        menuRequest.menuName(),
                        menuRequest.price(),
                        menuRequest.oneLiner(),
                        menuRequest.thumbnail()))
                .toList();

        menuRepository.saveAll(updatedMenus);
    }
}
