package ch.awae.moba2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Moba2Application {

    public static void main(String[] args) {
        SpringApplication.run(Moba2Application.class, args);
    }

}
