package ru.otus.devgroup.service;

import ru.otus.devgroup.domain.Request;
import ru.otus.devgroup.domain.Specification;

public interface AnalyticsService {
    Specification analyze(Request request);
}
