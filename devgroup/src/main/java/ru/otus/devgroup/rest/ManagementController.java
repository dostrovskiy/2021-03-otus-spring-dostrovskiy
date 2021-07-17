package ru.otus.devgroup.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.devgroup.domain.Release;
import ru.otus.devgroup.domain.Request;
import ru.otus.devgroup.service.ManagementService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/devgroup")
public class ManagementController {
    private final ManagementService managementService;

    @PostMapping("/order")
    public Collection<Release> order(@RequestBody Collection<Request> requests) {
        return managementService.develop(requests);
    }
}
