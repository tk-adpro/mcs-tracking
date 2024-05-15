package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

public class SiWuzzShippingStrategy implements ShippingStrategy {

    @Override
    public boolean validateTrackingCode(String trackingCode) {
        return trackingCode != null && trackingCode.matches("^SWZ-[A-Z]{12}$");
    }
}