package ru.otus.quiz.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Party {
    private final String name;
    private final int age;
}
