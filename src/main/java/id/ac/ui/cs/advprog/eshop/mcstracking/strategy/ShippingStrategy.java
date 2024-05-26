package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import org.apache.coyote.BadRequestException;

public interface ShippingStrategy {
    void validateTrackingCode(String trackingCode) throws BadRequestException;
}