package ru.otus.devgroup.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.devgroup.domain.Request;
import ru.otus.devgroup.domain.Specification;

@Service("analyticsService")
public class AnalyticsServiceImpl implements AnalyticsService {

    @SneakyThrows(InterruptedException.class)
    @Override
    public Specification analyze(Request request) {
        var requirements = request.getRequirements();
        Thread.sleep(requirements * 300L);
        var options = getOptions(requirements);
        return new Specification(options);
    }

    private int getOptions(int requirements) {
        return (int) Math.floor(RandomUtils.nextDouble(0.5, 1.5) * requirements);
    }
}
