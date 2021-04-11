package ru.otus.quiz.parsers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.otus.quiz.exceptions.CsvParsingException;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvParserImpl implements CsvParser {

    @Override
    public List<String> parse(Reader reader) {
        List<String> list = new ArrayList<>();
        String[] columns = {"column"};
        try {
            CSVParser csvParser = CSVFormat.DEFAULT.withHeader(columns).parse(reader);
            for (CSVRecord record : csvParser) {
                for (String column : columns)
                    list.add(record.get(column));
            }
        } catch (Exception e) {
            throw new CsvParsingException("Error parsing resource.", e);
        }
        return list;
    }
}
