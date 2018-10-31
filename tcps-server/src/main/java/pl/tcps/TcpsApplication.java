package pl.tcps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableJpaRepositories("pl.tcps.repositories")
@EnableResourceServer
public class TcpsApplication{
	public static void main(String[] args) {
		SpringApplication.run(TcpsApplication.class, args);
	}
}