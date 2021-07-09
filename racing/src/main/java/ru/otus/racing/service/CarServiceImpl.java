package ru.otus.racing.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.racing.domain.Car;
import ru.otus.racing.domain.Race;
import ru.otus.racing.exceptions.LackOfCarsException;

import java.util.*;

@Service("carService")
public class CarServiceImpl implements CarService {
    private static final List<Car> allCars = List.of(
            new Car("BMW", RandomUtils.nextInt(60, 90)),
            new Car("Toyota", RandomUtils.nextInt(70, 100)),
            new Car("Renault", RandomUtils.nextInt(50, 100)),
            new Car("McLaren", RandomUtils.nextInt(70, 110)),
            new Car("Ferrari", RandomUtils.nextInt(60, 120))
    );
    private final Deque<Car> cupCars = new ArrayDeque<>();

    public Collection<Race> prepare(Collection<Race> races) {
        cupCars.clear();
        var list = new ArrayList<>(allCars);
        Collections.shuffle(list);
        cupCars.addAll(list);
        return races;
    }

    public Race chooseCar(Race race) {
        race.setCar(getCar());
        return race;
    }

    private synchronized Car getCar() {
        if (cupCars.isEmpty())
            throw new LackOfCarsException();
        return cupCars.pollFirst();
    }
}
