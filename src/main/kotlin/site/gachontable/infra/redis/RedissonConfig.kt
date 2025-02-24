package site.gachontable.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
    @Value("\${spring.data.redis.host}")
    private val redisHost: String,

    @Value("\${spring.data.redis.port}")
    private val redisPort: Int,

    @Value("\${spring.data.redis.database}")
    private val redisDatabase: Int,
) {
    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().apply {
            address = "$REDISSON_HOST_PREFIX$redisHost:$redisPort/$redisDatabase"
        }
        return Redisson.create(config)
    }

    companion object {
        private const val REDISSON_HOST_PREFIX = "redis://"
    }
}
