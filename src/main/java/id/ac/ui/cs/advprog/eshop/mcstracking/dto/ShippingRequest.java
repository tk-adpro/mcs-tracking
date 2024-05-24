package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShippingRequest {
    private Long orderId;

    private Long paymentId;

    private Long userId;

    private String shippingMethod;

    private String receiptCode;
}
