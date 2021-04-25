package ru.otus.mybooks.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private final long id;
    private final String name;

    public Author(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        this(0, name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Author EMPTY_AUTHOR = new Author("Unknown");
}
