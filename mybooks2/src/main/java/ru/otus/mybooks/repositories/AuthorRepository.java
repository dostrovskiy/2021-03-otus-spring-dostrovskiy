package ru.otus.mybooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.mybooks.domain.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByName(String name);
}
