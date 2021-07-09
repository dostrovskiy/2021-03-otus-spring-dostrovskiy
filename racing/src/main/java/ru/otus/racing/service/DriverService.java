package ru.otus.racing.service;

import ru.otus.racing.domain.Race;

import java.util.Collection;

public interface DriverService {
    Collection<Race> prepare(Collection<Race> races);

    Race chooseDriver(Race race);
}
