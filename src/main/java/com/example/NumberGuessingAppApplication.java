package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NumberGuessingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumberGuessingAppApplication.class, args);
        new NumberGuessingGame().startGame();
    }
}
