package ru.otus.devgroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.devgroup.actuator.ProductionCounter;
import ru.otus.devgroup.domain.Release;
import ru.otus.devgroup.domain.Request;
import ru.otus.devgroup.integration.AppDev;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service("managementService")
public class ManagementServiceImpl implements ManagementService {
    private final AppDev appDev;
    private final ProductionCounter productionCounter;

    @Override
    public Collection<Release> develop(Collection<Request> requests) {
        var releases = appDev.develop(requests);
        var releaseList = new ArrayList<>(releases);
        var requestList = new ArrayList<>(requests);
        IntStream.range(0, releaseList.size())
                .forEach(i -> productionCounter.addOrderResults(
                        requestList.get(i).getRequirements(), releaseList.get(i).getFunctionality()));
        return releases;
    }
}
