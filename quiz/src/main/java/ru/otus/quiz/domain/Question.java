package ru.otus.quiz.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Question {
    private final String text;
    private final String rightAnswer;
    private final List<String> answers;
}
