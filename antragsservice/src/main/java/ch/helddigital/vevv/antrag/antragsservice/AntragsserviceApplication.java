// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/AntragsserviceApplication.java
package ch.helddigital.vevv.antrag.antragsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AntragsserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntragsserviceApplication.class, args);
    }
}
