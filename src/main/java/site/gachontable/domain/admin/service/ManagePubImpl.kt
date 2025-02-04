package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.domain.admin.port.in.ManagePub;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.menu.domain.Menu;
import site.gachontable.domain.menu.port.out.MenuRepository;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.pub.domain.Thumbnail;
import site.gachontable.presentation.admin.dto.request.PubManageRequest;
import site.gachontable.domain.pub.domain.repository.ThumbnailRepository;
import site.gachontable.independent.type.SuccessCode;

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
