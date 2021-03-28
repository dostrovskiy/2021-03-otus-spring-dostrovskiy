package ru.otus.quiz.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Question {
    private final String question;
    private final String answer;
}
