package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import org.apache.coyote.BadRequestException;

public class JTEShippingStrategy implements ShippingStrategy {

    @Override
    public void validateTrackingCode(String trackingCode) throws BadRequestException {
        if (trackingCode == null || !trackingCode.matches("^JTE-\\d{12}$")){
            throw new BadRequestException(ConstantUtils.INVALID_RECEIPT_CODE_MSG);
        }
    }
}