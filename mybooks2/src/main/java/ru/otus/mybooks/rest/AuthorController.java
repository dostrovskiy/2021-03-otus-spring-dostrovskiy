package ru.otus.mybooks.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.mybooks.dto.AuthorDto;
import ru.otus.mybooks.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/mybooks")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public List<AuthorDto> getAuthors() {
        return authorService.getAllAuthors();
    }
}
