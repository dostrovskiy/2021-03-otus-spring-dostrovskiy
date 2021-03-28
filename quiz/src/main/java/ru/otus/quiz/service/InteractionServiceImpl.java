package ru.otus.quiz.service;

import java.util.Scanner;

public class InteractionServiceImpl implements InteractionService{
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void say(String message) {
        System.out.println(message);
    }

    @Override
    public String ask(String message) {
        say(message);
        String text = scanner.nextLine();
        return text;
    }

    @Override
    public void finish() {
        scanner.close();
    }
}
