package ru.otus.devgroup.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.devgroup.domain.Application;
import ru.otus.devgroup.domain.Release;

@Service("testingService")
public class TestingServiceImpl implements TestingService {

    @SneakyThrows(InterruptedException.class)
    @Override
    public Release test(Application application) {
        var functions = application.getFunctions();
        Thread.sleep(functions * 100L);
        var functionality = getFunctionality(functions);
        return new Release(functionality);
    }
    private int getFunctionality(int functions) {
        return (int) Math.floor(RandomUtils.nextDouble(0.5, 1.5) * functions);
    }
}
