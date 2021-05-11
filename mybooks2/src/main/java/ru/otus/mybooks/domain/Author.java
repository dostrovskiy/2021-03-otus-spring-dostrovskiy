package ru.otus.mybooks.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = true, unique = false)
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public static final Author EMPTY_AUTHOR = new Author(0, "Unknown");
}
