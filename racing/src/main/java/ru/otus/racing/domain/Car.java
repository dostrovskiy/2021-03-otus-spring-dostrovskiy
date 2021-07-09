package ru.otus.racing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Car {
    private final String name;
    private final int speed;
}
