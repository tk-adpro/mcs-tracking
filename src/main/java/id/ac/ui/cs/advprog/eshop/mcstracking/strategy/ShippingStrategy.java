package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

public interface ShippingStrategy {
    boolean validateTrackingCode(String trackingCode);
}