package pl.tcps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("pl.tcps.repositories")
@EnableAutoConfiguration
public class TcpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpsApplication.class, args);
	}
}
