package site.gachontable.domain.pub.domain.repository

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.gachontable.domain.pub.domain.Pub
import site.gachontable.domain.pub.domain.Thumbnail

interface ThumbnailRepository : JpaRepository<Thumbnail, Long> {
    @Cacheable(key = "#pub.pubName", value = ["thumbnailsCache"], cacheManager = "thumbnailsCacheManager")
    @Query("SELECT t.url FROM thumbnail t WHERE t.pub = :pub")
    fun findUrlsByPub(pub: Pub): MutableList<String>

    fun deleteAllByPub(pub: Pub)
}
