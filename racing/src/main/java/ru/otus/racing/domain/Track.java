package ru.otus.racing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Track {
    private final String name;
    private final int length;
}
