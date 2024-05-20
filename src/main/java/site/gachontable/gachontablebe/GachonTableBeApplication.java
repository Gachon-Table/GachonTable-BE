package site.gachontable.gachontablebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GachonTableBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachonTableBeApplication.class, args);
	}

}
