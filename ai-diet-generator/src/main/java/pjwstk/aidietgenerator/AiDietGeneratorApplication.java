package pjwstk.aidietgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class AiDietGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiDietGeneratorApplication.class, args);
	}

	@GetMapping("/")
	public String helloPage(){
		return "Hello";
	}

}
