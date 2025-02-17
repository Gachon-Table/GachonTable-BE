package site.gachontable.infra.async

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfiguration {
    @Bean
    fun taskExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 8
            maxPoolSize = 16
            queueCapacity = 1000
            setThreadNamePrefix("Biztalk")
            initialize()
        }
    }
}
