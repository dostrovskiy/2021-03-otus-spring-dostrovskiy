package ru.otus.mybooks.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.mybooks.domain.Genre;
import ru.otus.mybooks.exception.GenreDaoFindException;
import ru.otus.mybooks.exception.GenreDaoGetAllException;
import ru.otus.mybooks.exception.GenreDaoGetByIdException;
import ru.otus.mybooks.exception.GenreDaoInsertException;

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
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", genre.getName());
            KeyHolder key = new GeneratedKeyHolder();
            jdbc.update("insert into genres (name) values (:name)", params, key);
            return new Genre(key.getKey().longValue(), genre.getName());
        } catch (Exception e) {
            throw new GenreDaoInsertException(e);
        }
    }

    @Override
    public Optional<Genre> find(Genre genre) {
        try {
            Map<String, Object> params = Map.of("name", genre.getName());
            List<Genre> list = jdbc.query("select * from genres where name = :name", params, new GenreMapper());
            return list.stream().findFirst();
        } catch (Exception e) {
            throw new GenreDaoFindException(e);
        }
    }

    @Override
    public Optional<Genre> getById(long id) {
        try {
            List<Genre> list = jdbc.query("select * from genres where id = :id", Map.of("id", id), new GenreMapper());
            return list.stream().findFirst();
        } catch (Exception e) {
            throw new GenreDaoGetByIdException(e);
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            return jdbc.query("select * from genres", new GenreMapper());
        } catch (Exception e) {
            throw new GenreDaoGetAllException(e);
        }
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
