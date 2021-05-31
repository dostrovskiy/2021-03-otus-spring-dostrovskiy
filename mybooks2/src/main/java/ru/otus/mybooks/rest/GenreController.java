package ru.otus.mybooks.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.mybooks.dto.AuthorDto;
import ru.otus.mybooks.dto.GenreDto;
import ru.otus.mybooks.service.AuthorService;
import ru.otus.mybooks.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/mybooks/genres")
    public List<GenreDto> getGenres() {
        return genreService.getAllGenres();
    }
}
