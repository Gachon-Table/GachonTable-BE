package site.gachontable.domain.admin.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.gachontable.domain.admin.exception.AdminNotFoundException
import site.gachontable.domain.admin.port.`in`.ManagePub
import site.gachontable.domain.admin.port.out.AdminRepository
import site.gachontable.domain.menu.domain.Menu
import site.gachontable.domain.menu.port.out.MenuRepository
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.domain.Thumbnail
import site.gachontable.domain.pub.domain.repository.ThumbnailRepository
import site.gachontable.independent.type.SuccessCode
import site.gachontable.infra.security.principal.AuthDetails
import site.gachontable.presentation.admin.dto.request.PubManageRequest
import site.gachontable.presentation.admin.dto.request.PubManageRequest.MenuRequest

@Service
class ManagePubImpl(
    private val adminRepository: AdminRepository,
    private val menuRepository: MenuRepository,
    private val thumbnailRepository: ThumbnailRepository,
) : ManagePub {
    @Caching(
        evict = [CacheEvict(
            value = arrayOf("menuCache"), cacheManager = "menuCacheManager", allEntries = true
        ), CacheEvict(
            value = arrayOf("thumbnailsCache"), cacheManager = "thumbnailsCacheManager", allEntries = true
        )]
    )
    @Transactional
    override fun execute(authDetails: AuthDetails, request: PubManageRequest): String {
        val pub: Pub = adminRepository.findById(authDetails.uuid)
            .orElse(throw AdminNotFoundException())
            .pub

        replaceThumbnails(request.thumbnails, pub)
        replaceMenus(request.menuRequests, pub)

        return SuccessCode.MANAGE_PUB_SUCCESS.message
    }

    private fun replaceThumbnails(thumbnails: MutableList<String>, pub: Pub) {
        thumbnailRepository.deleteAllByPub(pub)

        val newThumbnails: MutableList<Thumbnail> = thumbnails.stream()
            .map<Thumbnail> { url -> Thumbnail.create(url, pub) }
            .toList()

        thumbnailRepository.saveAll(newThumbnails)
    }

    private fun replaceMenus(request: MutableList<MenuRequest>, pub: Pub) {
        menuRepository.deleteAllByPub(pub)

        val updatedMenus = request.stream()
            .map<Menu> { menuRequest ->
                Menu.create(
                    pub,
                    menuRequest.menuName,
                    menuRequest.price,
                    menuRequest.oneLiner,
                    menuRequest.thumbnail
                )
            }
            .toList()

        menuRepository.saveAll(updatedMenus)
    }
}
