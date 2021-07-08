package ru.otus.racing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@Getter
@RequiredArgsConstructor
public class Driver {
    private final String name;
    private final int skill;
    private int mood;

    public void getReady() {
        mood = RandomUtils.nextInt(0, 30) - 15;
    }

    public int getSkill(){
        return skill + mood;
    }
}

