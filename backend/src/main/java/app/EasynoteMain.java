package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import app.entity.SpringSecurityAuditorAware;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class EasynoteMain {
	
	@Bean
	public AuditorAware<String> auditorAware(){
		return new SpringSecurityAuditorAware();
	}

	public static void main(String[] args) {
		SpringApplication.run(EasynoteMain.class, args);
	}

}
