package ru.otus.devgroup.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.devgroup.domain.Release;
import ru.otus.devgroup.domain.Request;

import java.util.Collection;

@MessagingGateway
public interface AppDev {

    @Gateway(requestChannel = "requestsChannel", replyChannel = "appsChannel")
    Collection<Release> develop(Collection<Request> requests);
}
