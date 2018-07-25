package pl.tcps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TcpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpsApplication.class, args);
	}
}
