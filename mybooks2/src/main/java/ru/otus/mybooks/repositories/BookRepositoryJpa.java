package ru.otus.mybooks.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.exception.BookRepositoryBookNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Book book = Optional.ofNullable(em.find(Book.class, id))
                .orElseThrow(() -> new BookRepositoryBookNotFoundException(id));
        book.setAuthors(List.of());
        book.setGenres(List.of());
        em.remove(book);
    }
}
