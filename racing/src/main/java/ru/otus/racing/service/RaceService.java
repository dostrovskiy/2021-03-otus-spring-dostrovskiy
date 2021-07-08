package ru.otus.racing.service;

import org.springframework.stereotype.Service;
import ru.otus.racing.domain.Mood;
import ru.otus.racing.domain.Race;

@Service
public class RaceService {

    public Race race(Race race) throws InterruptedException {
        System.out.printf("%s in %s mood on %s starting!%n",
                race.getDriver().getName(), Mood.getMood(race.getDriver().getMood()), race.getCar().getName());
        var duration = race.getDistance() * 10 / (race.getSpeed() * 3600 / 1000);
        Thread.sleep(duration * 50L);
        race.setDuration(duration);
        return race;
    }
}
