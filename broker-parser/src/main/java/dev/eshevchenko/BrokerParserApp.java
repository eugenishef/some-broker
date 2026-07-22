package dev.eshevchenko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BrokerParserApp {

    public static void main(String[] args) {
        SpringApplication.run(BrokerParserApp.class, args);
    }
}