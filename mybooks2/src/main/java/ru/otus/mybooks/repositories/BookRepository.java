package ru.otus.mybooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.mybooks.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
