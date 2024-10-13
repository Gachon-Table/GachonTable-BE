package site.gachontable.gachontablebe.global.config.redis;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class RedissonCacheConfig {

    @Primary
    @Bean
    public CacheManager thumbnailsCacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        config.put("thumbnailsCache", new CacheConfig(24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }

    @Bean
    public CacheManager menuCacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        config.put("menuCache", new CacheConfig(24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
