package com.neflodev.expensestrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class ExpensesTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpensesTrackerApiApplication.class, args);
    }

    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }

}
