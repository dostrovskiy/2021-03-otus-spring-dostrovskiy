package ru.otus.mybooks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.mybooks.domain.Review;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class BookReviewsDto {
    private final String bookInfo;
    private final List<Review> reviews;
}
