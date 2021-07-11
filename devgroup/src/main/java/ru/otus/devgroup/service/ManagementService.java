package ru.otus.devgroup.service;

import ru.otus.devgroup.domain.Release;
import ru.otus.devgroup.domain.Request;

import java.util.Collection;

public interface ManagementService {
    Collection<Release> develop(Collection<Request> requests);
}
