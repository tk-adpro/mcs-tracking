package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import org.apache.coyote.BadRequestException;

public class GoBekShippingStrategy implements ShippingStrategy {

    @Override
    public void validateTrackingCode(String trackingCode) throws BadRequestException {
        if (trackingCode == null || !trackingCode.matches("^GBK-[A-Z0-9]{12}$")){
            throw new BadRequestException(ConstantUtils.INVALID_RECEIPT_CODE_MSG);
        }
    }
}