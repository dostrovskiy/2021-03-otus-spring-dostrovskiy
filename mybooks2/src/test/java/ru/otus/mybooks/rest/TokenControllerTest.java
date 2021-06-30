package ru.otus.mybooks.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Класс TokenController должен ")
class TokenControllerTest {
    public static final String READER_CREDENTIALS = "cmVhZGVyOnBhc3M=";

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("вернуть действующий токен.")
    void shouldGetValidToken() throws Exception {
        var result = mvc.perform(post("/token")
                .header("Authorization", "Basic " + READER_CREDENTIALS))
                .andExpect(status().isOk())
                .andReturn();
        var token = result.getResponse().getContentAsString();

        mvc.perform(get("/mybooks/genres")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}