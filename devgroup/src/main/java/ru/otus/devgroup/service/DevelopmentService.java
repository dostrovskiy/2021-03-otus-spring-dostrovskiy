package ru.otus.devgroup.service;

import ru.otus.devgroup.domain.Application;
import ru.otus.devgroup.domain.Specification;

public interface DevelopmentService {
    Application develop(Specification specification);
}
