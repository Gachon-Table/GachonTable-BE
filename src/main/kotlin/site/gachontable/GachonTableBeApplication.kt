package site.gachontable

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class GachonTableBeApplication

fun main(args: Array<String>) {
    runApplication<GachonTableBeApplication>(*args)
}
