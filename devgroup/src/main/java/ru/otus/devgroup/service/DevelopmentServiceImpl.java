package ru.otus.devgroup.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.devgroup.domain.Application;
import ru.otus.devgroup.domain.Specification;

@Service("developmentService")
public class DevelopmentServiceImpl implements DevelopmentService {

    @SneakyThrows(InterruptedException.class)
    @Override
    public Application develop(Specification specification) {
        var options = specification.getOptions();
        Thread.sleep(options * 200L);
        var functions = getFunctions(options);
        return new Application(functions);
    }

    private int getFunctions(int options) {
        return (int) Math.floor(RandomUtils.nextDouble(0.5, 1.5) * options);
    }
}
