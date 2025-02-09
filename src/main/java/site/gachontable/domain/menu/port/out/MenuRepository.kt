package site.gachontable.domain.menu.port.out

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import site.gachontable.domain.menu.domain.Menu
import site.gachontable.domain.pub.domain.Pub

interface MenuRepository : JpaRepository<Menu, Int> {
    @Cacheable(key = "#pub.pubName", value = ["menuCache"], cacheManager = "menuCacheManager")
    fun findAllByPub(pub: Pub): MutableList<Menu>

    fun deleteAllByPub(pub: Pub)
}
