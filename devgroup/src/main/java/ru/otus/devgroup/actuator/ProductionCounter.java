package ru.otus.devgroup.actuator;

public interface ProductionCounter {
    void addOrderResults(int requirements, int functionality);

    int getComplianceWithRequirements();

    int getOrdersCount();
}
