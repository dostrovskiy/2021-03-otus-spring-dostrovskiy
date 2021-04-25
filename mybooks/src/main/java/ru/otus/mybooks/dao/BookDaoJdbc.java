package ru.otus.mybooks.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.domain.Book;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.exception.*;
import ru.otus.mybooks.record.BookRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Book insert(Book book) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("title", book.getTitle());
            params.addValue("author_id", book.getAuthor().getId());
            params.addValue("genre_id", book.getGenre().getId());
            KeyHolder key = new GeneratedKeyHolder();
            jdbc.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                    params,
                    key);
            return new Book(key.getKey().longValue(), book.getTitle(), book.getAuthor(), book.getGenre());
        } catch (Exception e) {
            throw new BookDaoInsertException(e);
        }
    }

    @Override
    public Book update(Book book) {
        try {
            Map<String, Object> params = Map.of(
                    "id", book.getId(),
                    "title", book.getTitle(),
                    "author_id", book.getAuthor().getId(),
                    "genre_id", book.getGenre().getId());
            jdbc.update("update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id", params);
            return new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre());
        } catch (Exception e) {
            throw new BookDaoUpdateException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            jdbc.update("delete from books where id = :id", Map.of("id", id));
        } catch (Exception e) {
            throw new BookDaoDeleteByIdException(e);
        }
    }

    @Override
    public Optional<Book> find(Book book) {
        try {
            Map<String, Object> params = Map.of(
                    "title", book.getTitle(),
                    "author_id", book.getAuthor().getId(),
                    "genre_id", book.getGenre().getId());
            List<BookRecord> list = jdbc.query(
                    "select * from books where title = :title and author_id = :author_id and genre_id = :genre_id",
                    params,
                    new BookMapper());
            return list.stream()
                    .findFirst()
                    .map(r -> new Book(r.getId(), r.getTitle(),
                            authorDao.getById(r.getAuthorId()).orElse(Author.EMPTY_AUTHOR),
                            genreDao.getById(r.getGenreId()).orElse(Genre.EMPTY_GENRE)));
        } catch (Exception e) {
            throw new BookDaoFindException(e);
        }
    }

    @Override
    public Optional<Book> getById(long id) {
        try {
            List<BookRecord> l = jdbc.query("select * from books where id = :id", Map.of("id", id), new BookMapper());
            return l.stream()
                    .map(r -> new Book(r.getId(),
                            r.getTitle(),
                            authorDao.getById(r.getAuthorId()).orElse(Author.EMPTY_AUTHOR),
                            genreDao.getById(r.getGenreId()).orElse(Genre.EMPTY_GENRE)))
                    .findFirst();
        } catch (Exception e) {
            throw new BookDaoGetByIdException(e);
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            List<BookRecord> l = jdbc.query("select * from books", new BookMapper());
            return l.stream()
                    .map(r -> new Book(r.getId(),
                            r.getTitle(),
                            authorDao.getById(r.getAuthorId()).orElse(Author.EMPTY_AUTHOR),
                            genreDao.getById(r.getGenreId()).orElse(Genre.EMPTY_GENRE)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookDaoGetAllException(e);
        }
    }

    private static class BookMapper implements RowMapper<BookRecord> {
        @Override
        public BookRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            long genreId = rs.getLong("genre_id");
            return new BookRecord(id, title, authorId, genreId);
        }
    }
}
