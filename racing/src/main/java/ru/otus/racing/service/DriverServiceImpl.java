package ru.otus.racing.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.racing.domain.Driver;
import ru.otus.racing.domain.Race;
import ru.otus.racing.exceptions.LackOfDriversException;

import java.util.*;

@Service("driverService")
public class DriverServiceImpl implements DriverService {
    private static final List<Driver> allDrivers = List.of(
            new Driver("Sebastian Vettel", RandomUtils.nextInt(0, 5)),
            new Driver("Lewis Hamilton", RandomUtils.nextInt(0, 30)),
            new Driver("Fernando Alonso", RandomUtils.nextInt(0, 30) - 15),
            new Driver("Ayrton Senna", RandomUtils.nextInt(0, 20) - 10),
            new Driver("Michael Schumacher", RandomUtils.nextInt(0, 25) - 5)
    );
    private final Deque<Driver> cupDrivers = new ArrayDeque<>();

    public Collection<Race> prepare(Collection<Race> races) {
        cupDrivers.clear();
        var list = new ArrayList<>(allDrivers);
        Collections.shuffle(list);
        cupDrivers.addAll(list);
        return races;
    }

    public Race chooseDriver(Race race) {
        race.setDriver(getDriver());
        return race;
    }

    private synchronized Driver getDriver() {
        if (cupDrivers.isEmpty())
            throw new LackOfDriversException();
        Driver driver = cupDrivers.pollFirst();
        driver.getReady();
        return driver;
    }
}
