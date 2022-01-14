package first.study.exceptiontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExceptiontestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExceptiontestApplication.class, args);
	}

}
