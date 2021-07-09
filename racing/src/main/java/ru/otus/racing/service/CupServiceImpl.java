package ru.otus.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.racing.domain.Race;
import ru.otus.racing.domain.Track;
import ru.otus.racing.exceptions.RacingException;
import ru.otus.racing.integration.Racing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service("cupService")
public class CupServiceImpl implements CupService {
    private final Racing racing;
    private final List<Track> allTracks = List.of(
            new Track("Hungaroring", 4381),
            new Track("Circuit de Monaco", 3337),
            new Track("Red Bull Ring", 4318),
            new Track("Silverstone Circuit", 5891),
            new Track("Suzuka Circuit", 5807)
    );

    @Scheduled(fixedDelay = 1000)
    public void startRacingCup() {
        var track = getTrack();
        showGreeting(track);
        var races = getRaces(track);
        var results = racing.start(races);
        showResults(results, track);
    }

    private void showResults(Collection<Race> results, Track track) {
        System.out.println("--------------- " + track.getName() + " race results ---------------");
        var list = results.stream()
                .sorted(Comparator.comparing(Race::getDuration)) // Comparator.comparing(Race::getDuration, Comparator.reverseOrder())
                .map(r -> String.format("%s on %s rushing at %d km/h finished the race in %d sec",
                        r.getDriver().getName(), r.getCar().getName(), r.getSpeed(), r.getDuration()))
                .collect(Collectors.toList());
        System.out.println(IntStream
                .range(0, list.size())
                .mapToObj(i -> String.format("%s and took the %d place%n", list.get(i), i + 1))
                .collect(Collectors.joining("")));
        System.out.println();
    }

    @SneakyThrows
    private void showGreeting(Track track) {
        System.out.println("*************** !!!STARTING " + track.getName() + " RACING CUP!!! ***************");
        Thread.sleep(3000);
    }

    private Track getTrack() {
        return allTracks.get(RandomUtils.nextInt(0, allTracks.size()));
    }

    private Collection<Race> getRaces(Track track) {
        var races = new ArrayList<Race>();
        for (int i = 0; i < RandomUtils.nextInt(3, 5); i++) {
            races.add(new Race(track));
        }
        return races;
    }
}
