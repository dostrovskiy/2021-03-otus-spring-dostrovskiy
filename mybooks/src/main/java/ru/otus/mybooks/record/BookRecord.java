package ru.otus.mybooks.record;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookRecord {
    private final long id;
    private final String title;
    private final long authorId;
    private final long genreId;
}
