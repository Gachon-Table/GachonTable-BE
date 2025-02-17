package site.gachontable.infra.redis

import org.redisson.api.RedissonClient
import org.redisson.spring.cache.CacheConfig
import org.redisson.spring.cache.RedissonSpringCacheManager
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@EnableCaching
@Configuration
class RedissonCacheConfig {
    @Primary
    @Bean
    fun thumbnailsCacheManager(redissonClient: RedissonClient): CacheManager {
        val config: MutableMap<String, CacheConfig> = HashMap<String, CacheConfig>()

        config.put(
            "thumbnailsCache", CacheConfig((5 * 60 * 60 * 1000).toLong(), (2 * 60 * 60 * 1000).toLong())
        )
        return RedissonSpringCacheManager(redissonClient, config)
    }

    @Bean
    fun menuCacheManager(redissonClient: RedissonClient): CacheManager {
        val config: MutableMap<String, CacheConfig> = HashMap<String, CacheConfig>()

        config.put(
            "menuCache", CacheConfig((5 * 60 * 60 * 1000).toLong(), (2 * 60 * 60 * 1000).toLong())
        )
        return RedissonSpringCacheManager(redissonClient, config)
    }
}
