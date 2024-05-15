package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

public class GoBekShippingStrategy implements ShippingStrategy {

    @Override
    public boolean validateTrackingCode(String trackingCode) {
        return trackingCode != null && trackingCode.matches("^GBK-[A-Z0-9]{12}$");
    }
}