package pjwstk.aidietgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

@EnableScheduling
@SpringBootApplication
public class AiDietGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(AiDietGeneratorApplication.class, args);
	}
}
