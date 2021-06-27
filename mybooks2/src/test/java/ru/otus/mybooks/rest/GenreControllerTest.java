package ru.otus.mybooks.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.mybooks.dto.GenreDto;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Класс GenreController должен ")
class GenreControllerTest {
    public static final String READER_CREDENTIALS = "cmVhZGVyOnBhc3M=";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("вернуть все жанры.")
    void shouldGetGenres() throws Exception {
        var expGenres = List.of(new GenreDto(1, "Пьеса"));

        var result = this.mvc.perform(post("/token")
                .header("Authorization", "Basic " + READER_CREDENTIALS))
                .andExpect(status().isOk())
                .andReturn();
        var token = result.getResponse().getContentAsString();

        mvc.perform(get("/mybooks/genres")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expGenres)));
    }
}