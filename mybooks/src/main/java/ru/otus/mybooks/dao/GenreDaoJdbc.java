package ru.otus.mybooks.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Genre insert(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update("insert into genres (name) values (:name)", params, key);
        return new Genre(key.getKey().longValue(), genre.getName());
    }

    @Override
    public Optional<Genre> find(Genre genre) {
        Map<String, Object> params = Map.of("name", genre.getName());
        List<Genre> list = jdbc.query("select id, name from genres where name = :name", params, new GenreMapper());
        return list.stream().findFirst();
    }

    @Override
    public Optional<Genre> getById(long id) {
        List<Genre> list = jdbc.query("select id, name from genres where id = :id", Map.of("id", id), new GenreMapper());
        return list.stream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, name from genres", new GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
