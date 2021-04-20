package ru.otus.quiz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;


@Service
public class InteractionServiceConsole implements InteractionService {
    private final PrintStream output;
    private final Scanner scanner;

    public InteractionServiceConsole(@Value("#{T(java.lang.System).in}") InputStream input,
                                     @Value("#{T(java.lang.System).out}") PrintStream output) {
        this.output = output;
        scanner = new Scanner(input);
    }

    @Override
    public void say(String message) {
        output.println(message);
    }

    @Override
    public String ask(String message) {
        output.print(message + " ");
        return scanner.nextLine();
    }

    @Override
    public void finish() {
        scanner.close();
    }
}
