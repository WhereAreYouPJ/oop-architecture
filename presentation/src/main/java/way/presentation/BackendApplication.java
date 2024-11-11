package way.presentation;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = "way.application.infrastructure")
@EnableJpaRepositories(basePackages = "way.application.infrastructure")
@ComponentScan(basePackages = {
	"way.application.utils",
	"way.application.core",
	"way.application.service",
	"way.application.domain",
	"way.application.infrastructure",
	"way.presentation"
})
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
