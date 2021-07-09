package ru.otus.racing.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.racing.domain.Race;

import java.util.Collection;

@MessagingGateway
public interface Racing {

    @Gateway(requestChannel = "startChannel", replyChannel = "finishChannel")
    Collection<Race> start(Collection<Race> races);
}
