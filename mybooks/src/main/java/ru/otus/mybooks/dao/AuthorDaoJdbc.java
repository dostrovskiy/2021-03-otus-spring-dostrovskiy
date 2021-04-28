package ru.otus.mybooks.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Author;

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
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", author.getName());
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update("insert into authors (name) values (:name)", params, key);
        return Author.builder().id(key.getKey().longValue()).name(author.getName()).build();
    }

    @Override
    public Optional<Author> find(Author author) {
        Map<String, Object> params = Map.of("name", author.getName());
        List<Author> list = jdbc.query("select id, name from authors where name = :name", params, new AuthorMapper());
        return list.stream().findFirst();
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from authors", new AuthorMapper());
    }

    @Override
    public Optional<Author> getById(long id) {
        List<Author> list = jdbc.query("select id, name from authors where id = :id", Map.of("id", id), new AuthorMapper());
        return list.stream().findFirst();
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
