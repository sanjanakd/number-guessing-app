package com.example;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Scanner;

@Component
public class ConsoleService {


    public String readUserInput(InputStream inputStream) {
        Scanner keyboard = new Scanner(inputStream);
        return keyboard.nextLine();
    }

    public void printItOnConsole(String output) {
        System.out.print(output);
    }

}
