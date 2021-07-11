package ru.otus.devgroup.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.devgroup.domain.Order;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class ProductionCounterImpl implements ProductionCounter {
    private final int ordersHistoryDepth;
    private final Deque<Order> orders;

    public ProductionCounterImpl(@Value("${devgroup.orders-history-depth}") int ordersHistoryDepth) {
        this.ordersHistoryDepth = ordersHistoryDepth;
        this.orders = new ArrayDeque<>(ordersHistoryDepth);
    }

    @Override
    public void addOrderResults(int requirements, int functionality) {
        if (orders.size() >= ordersHistoryDepth)
            orders.poll();
        orders.offerLast(new Order(requirements, functionality));
    }

    @Override
    public int getComplianceWithRequirements() {
        var diff = orders.stream()
                .map(o -> o.getFunctionalities() - o.getRequirements())
                .reduce(0, Integer::sum);
        return orders.isEmpty() ? 0 : diff / orders.size();
    }

    @Override
    public int getOrdersCount() {
        return orders.size();
    }
}
