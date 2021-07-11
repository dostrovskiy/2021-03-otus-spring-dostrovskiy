package ru.otus.devgroup.service;

import ru.otus.devgroup.domain.Application;
import ru.otus.devgroup.domain.Release;

public interface TestingService {
    Release test(Application application);
}
