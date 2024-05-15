package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShippingStrategyFactory {

    private final Map<String, ShippingStrategy> strategies = new HashMap<>();

    public ShippingStrategyFactory() {
        strategies.put("JTE", new JTEShippingStrategy());
        strategies.put("Go-bek", new GoBekShippingStrategy());
        strategies.put("SiWuzz", new SiWuzzShippingStrategy());
    }

    public ShippingStrategy getStrategy(String shippingMethod) {
        return strategies.get(shippingMethod);
    }
}