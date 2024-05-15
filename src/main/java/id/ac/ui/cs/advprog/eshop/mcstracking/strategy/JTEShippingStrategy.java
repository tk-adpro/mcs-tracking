package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

public class JTEShippingStrategy implements ShippingStrategy {

    @Override
    public boolean validateTrackingCode(String trackingCode) {
        return trackingCode != null && trackingCode.matches("^JTE-\\d{12}$");
    }
}