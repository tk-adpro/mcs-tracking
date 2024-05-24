package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class ShippingStrategyFactory {

    private Map<String, ShippingStrategy> strategies;

    public ShippingStrategyFactory() {
        strategies = new HashMap<>();
        strategies.put("JTE", new JTEShippingStrategy());
        strategies.put("Go-bek", new GoBekShippingStrategy());
        strategies.put("SiWuzz", new SiWuzzShippingStrategy());
    }

    public ShippingStrategy getStrategy(String shippingMethod) {
        log.info("Amount of strategies available: {}", strategies.size());
        log.info(shippingMethod);
        log.info(strategies.get("JTE"));
        log.info(strategies.get("Go-bek"));
        return strategies.get(shippingMethod);
    }
}