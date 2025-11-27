package kspo.onfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnfitApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnfitApplication.class, args);
    }

}
