package ru.otus.mybooks.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GenreDto {
    private final long id;
    private final String name;

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
