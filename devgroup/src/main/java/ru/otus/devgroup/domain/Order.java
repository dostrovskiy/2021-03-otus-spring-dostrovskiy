package ru.otus.devgroup.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Order {
    private final int requirements;
    private final int functionalities;
}
