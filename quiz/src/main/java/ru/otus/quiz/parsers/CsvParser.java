package ru.otus.quiz.parsers;

import java.io.Reader;
import java.util.List;

public interface CsvParser {
    List<String> parse(Reader reader);
}
