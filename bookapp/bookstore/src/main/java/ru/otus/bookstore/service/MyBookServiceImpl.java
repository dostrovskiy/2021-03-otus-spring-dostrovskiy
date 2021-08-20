package ru.otus.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.bookstore.dto.BookDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBookServiceImpl implements MyBookService {
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final RestTemplate rest;
    private String savedToken;
    @Value("${mybooks.url}")
    private String url;
    @Value("${mybooks.auth}")
    private String auth;

    @Override
    public List<BookDto> getBooks() {
        var books = new ArrayList<BookDto>();
        var token = getToken();
        if (token != null && !token.isEmpty()) {
            var circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
            var headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            var response = circuitBreaker.run(() ->
                    rest.exchange("http://" + url + "/mybooks/books", HttpMethod.GET, new HttpEntity<String>(headers), BookDto[].class),
                    throwable -> ResponseEntity.ok(new BookDto[]{})
            );
            books.addAll(response.getStatusCode().equals(HttpStatus.OK) && (response.getBody() != null) ?
                    Arrays.asList(response.getBody()) :
                    Collections.emptyList());
        }
        return books;
    }

    @Override
    public String getToken() {
        if (savedToken == null || savedToken.isEmpty()) {
            var circuitBreaker = circuitBreakerFactory.create("tokenCircuitBreaker");
            var headers = new HttpHeaders();
            var encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
            headers.set("Authorization", "Basic " + new String(encodedAuth));
            var authEntity = new HttpEntity<>(headers);
            var response = circuitBreaker.run(() ->
                    rest.exchange("http://" + url + "/token", HttpMethod.POST, authEntity, String.class),
                    throwable -> ResponseEntity.ok(Strings.EMPTY)
            );
            if (response.getStatusCode().equals(HttpStatus.OK) && (response.getBody() != null))
                savedToken = response.getBody();
        }
        return savedToken;
    }
}
