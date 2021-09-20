package myfuture.gifticonhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GifticonRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GifticonRepositoryApplication.class, args);
	}

}
