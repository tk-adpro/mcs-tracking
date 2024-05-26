package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentVerificationResponse {
    private String status;
    private String value;
}
