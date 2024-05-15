package id.ac.ui.cs.advprog.eshop.mcstracking.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class PaymentVerificationRequest {
    private Long paymentId;
    private boolean isVerified;
}
