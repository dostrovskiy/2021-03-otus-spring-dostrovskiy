package ru.otus.mybooks.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDto {
    private final long id;
    private final String text;

    @Override
    public String toString() {
        return text + " (" + id + ")";
    }
}
