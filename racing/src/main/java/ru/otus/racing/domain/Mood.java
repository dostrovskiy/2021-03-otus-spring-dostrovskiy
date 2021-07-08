package ru.otus.racing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.racing.exceptions.MoodNotFoundException;

import java.util.List;

@Getter
@AllArgsConstructor
public class Mood {
    private final int min;
    private final int max;
    private final String name;

    public static final List<Mood> MOODS = List.of(
            new Mood(-100, -21, "extremely bad"),
            new Mood(-20, -16, "very bad"),
            new Mood(-15, -11, "bad"),
            new Mood(-10, -6, "poor"),
            new Mood(-5, 5, "so-so"),
            new Mood(6, 10, "positive"),
            new Mood(11, 15, "great"),
            new Mood(16, 20, "cheerful"),
            new Mood(21, 100, "extremely high")
    );

    public static String getMood(int value) {
        return MOODS.stream()
                .filter(m -> (value >= m.getMin()) && (value <= m.getMax()))
                .map(Mood::getName)
                .findFirst()
                .orElseThrow(MoodNotFoundException::new);
    }
}
