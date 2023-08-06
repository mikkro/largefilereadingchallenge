package com.kyotu.largefilereadingchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LargeFileReadingChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargeFileReadingChallengeApplication.class, args);
    }

}
