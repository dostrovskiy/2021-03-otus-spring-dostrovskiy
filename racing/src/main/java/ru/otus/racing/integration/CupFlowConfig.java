package ru.otus.racing.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import java.util.concurrent.Executors;

@Configuration
public class CupFlowConfig {

    @Bean
    public QueueChannel itemsChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel finishChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow cupFlow() {
        return IntegrationFlows.from("startChannel")
                .handle("carService", "prepare")
                .handle("driverService", "prepare")
                .split()
                .channel(MessageChannels.executor(Executors.newFixedThreadPool(10)))
                .handle("carService", "chooseCar")
                .handle("driverService", "chooseDriver")
                .handle("raceService", "race")
                .aggregate()
                .channel("finishChannel")
                .get();
    }
}
