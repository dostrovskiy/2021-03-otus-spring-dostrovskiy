package ru.otus.devgroup.actuator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class FunctionalityHealthIndicator implements HealthIndicator {
    private final ProductionCounter productionCounter;
    @Value("${devgroup.compliance.threshold}")
    private int complianceThreshold;

    @Override
    public Health health() {
        var details = Map.of(
                "Orders", productionCounter.getOrdersCount(),
                "Salary ratio", String.format("%1.1f", getSalaryRatio(productionCounter.getComplianceWithRequirements())));
        if (productionCounter.getComplianceWithRequirements() < complianceThreshold)
            return Health.down().withDetails(details).build();
        else
            return Health.up().withDetails(details).build();
    }

    private double getSalaryRatio(int complianceWithRequirements) {
        var n = complianceWithRequirements;
        if (complianceWithRequirements < -5)
            n = -5;
        if (complianceWithRequirements > 5)
            n = 5;
        return 1.0 + n / 10.0;
    }
}
