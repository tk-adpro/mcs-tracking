package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class PaymentVerificationRequest {
    private Long paymentId;
    private boolean isVerified;
}
