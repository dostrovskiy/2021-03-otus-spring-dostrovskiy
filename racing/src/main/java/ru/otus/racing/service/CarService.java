package ru.otus.racing.service;

import ru.otus.racing.domain.Race;

import java.util.Collection;

public interface CarService {
    Collection<Race> prepare(Collection<Race> races);

    Race chooseCar(Race race);
}
