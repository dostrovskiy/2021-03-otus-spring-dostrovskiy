package ru.otus.racing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Race {
    private final Track track;
    private Car car;
    private Driver driver;
    private int duration;

    public int getSpeed() {
        return car.getSpeed() + driver.getSkill();
    }

    public int getDistance() {
        return track.getLength();
    }
}
