package site.gachontable.gachontablebe.global.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    private static final String REDISSON_HOST_PREFIX = "rediss://";
    private static final String REDISSON_PRIMARY_PREFIX = "gachontable-001.";
    private static final String REDISSON_REPLICA_1_PREFIX = "gachontable-002.";
    private static final String REDISSON_REPLICA_2_PREFIX = "gachontable-003.";
    private static final String REDISSON_REPLICA_3_PREFIX = "gachontable-004.";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useReplicatedServers()
                .addNodeAddress(
                        REDISSON_HOST_PREFIX + REDISSON_PRIMARY_PREFIX + redisHost + ":" + redisPort + "/" + redisDatabase,
                        REDISSON_HOST_PREFIX + REDISSON_REPLICA_1_PREFIX + redisHost + ":" + redisPort + "/" + redisDatabase,
                        REDISSON_HOST_PREFIX + REDISSON_REPLICA_2_PREFIX + redisHost + ":" + redisPort + "/" + redisDatabase,
                        REDISSON_HOST_PREFIX + REDISSON_REPLICA_3_PREFIX + redisHost + ":" + redisPort + "/" + redisDatabase
                )
                .setPassword(redisPassword);
        return Redisson.create(config);
    }
}
