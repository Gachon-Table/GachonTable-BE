package site.gachontable.gachontablebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GachonTableBeApplication {
	// TODO: lockKey API별로 분류해서 정리 및 제공

	public static void main(String[] args) {
		SpringApplication.run(GachonTableBeApplication.class, args);
	}

}
