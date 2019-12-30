package ch.awae.moba2.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MobaProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobaProxyApplication.class, args);
    }

}
