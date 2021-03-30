package ru.otus.quiz.service;

public interface InteractionService {
    void say(String message);
    String ask(String message);
    void finish();
}
