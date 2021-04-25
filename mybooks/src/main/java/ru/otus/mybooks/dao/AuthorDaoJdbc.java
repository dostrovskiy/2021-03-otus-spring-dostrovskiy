package ru.otus.mybooks.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Author;
import ru.otus.mybooks.exception.AuthorDaoFindException;
import ru.otus.mybooks.exception.AuthorDaoGetAllException;
import ru.otus.mybooks.exception.AuthorDaoGetByIdException;
import ru.otus.mybooks.exception.AuthorDaoInsertException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Author insert(Author author) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", author.getName());
            KeyHolder key = new GeneratedKeyHolder();
            jdbc.update("insert into authors (name) values (:name)", params, key);
            return Author.builder().id(key.getKey().longValue()).name(author.getName()).build();
        } catch (Exception e) {
            throw new AuthorDaoInsertException(e);
        }
    }

    @Override
    public Optional<Author> find(Author author) {
        try {
            Map<String, Object> params = Map.of("name", author.getName());
            List<Author> list = jdbc.query("select * from authors where name = :name", params, new AuthorMapper());
            return list.stream().findFirst();
        } catch (Exception e) {
            throw new AuthorDaoFindException(e);
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            return jdbc.query("select * from authors", new AuthorMapper());
        } catch (Exception e) {
            throw new AuthorDaoGetAllException(e);
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        try {
            List<Author> list = jdbc.query("select * from authors where id = :id", Map.of("id", id), new AuthorMapper());
            return list.stream().findFirst();
        } catch (Exception e) {
            throw new AuthorDaoGetByIdException(e);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Author(id, name);
        }
    }
}
