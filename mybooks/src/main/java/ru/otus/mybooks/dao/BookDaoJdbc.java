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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update("insert into books(title, author_id, genre_id) " +
                        "values (:title, :author_id, :genre_id)",
                params,
                key);
        return new Book(key.getKey().longValue(), book.getTitle(), book.getAuthor(), book.getGenre());
    }

    @Override
    public void update(Book book) {
        jdbc.update("update books " +
                        "set title = :title, " +
                        "author_id = :author_id, " +
                        "genre_id = :genre_id " +
                        "where id = :id",
                Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()));
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    @Override
    public Optional<Book> find(Book book) {
        return jdbc.query("select b.id, " +
                        "b.title, " +
                        "a.id as author_id, " +
                        "a.name as author_name, " +
                        "g.id as genre_id, " +
                        "g.name as genre_name " +
                        "from books b " +
                        "left join authors a on a.id = b.author_id " +
                        "left join genres g on g.id = b.genre_id " +
                        "where b.title = :title " +
                        "and b.author_id = :author_id " +
                        "and b.genre_id = :genre_id",
                Map.of("title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()),
                new BookMapper()).stream().findFirst();
    }

    @Override
    public Optional<Book> getById(long id) {
        return jdbc.query("select b.id, " +
                "b.title, " +
                "a.id as author_id, " +
                "a.name as author_name, " +
                "g.id as genre_id, " +
                "g.name as genre_name " +
                "from books b " +
                "left join authors a on a.id = b.author_id " +
                "left join genres g on g.id = b.genre_id " +
                "where b.id = :id", Map.of("id", id), new BookMapper()).stream().findFirst();
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select b.id, " +
                "b.title, " +
                "a.id as author_id, " +
                "a.name as author_name, " +
                "g.id as genre_id, " +
                "g.name as genre_name " +
                "from books b " +
                "left join authors a on a.id = b.author_id " +
                "left join genres g on g.id = b.genre_id", new BookMapper());
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = rs.getLong("author_id") > 0 ?
                    Author.builder()
                            .id(rs.getLong("author_id"))
                            .name(rs.getString("author_name")).build() :
                    Author.EMPTY_AUTHOR;
            Genre genre = rs.getLong("genre_id") > 0 ?
                    new Genre(rs.getLong("genre_id"),
                            rs.getString("genre_name")) :
                    Genre.EMPTY_GENRE;
            return new Book(rs.getLong("id"), rs.getString("title"), author, genre);
        }
    }
}
