package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentVerificationRequestTest {

    private PaymentVerificationRequest paymentVerificationRequest;

    @BeforeEach
    void setUp() {
        Long longDummy = 1L;
        boolean boolDummy = true;
        this.paymentVerificationRequest = new PaymentVerificationRequest();
        this.paymentVerificationRequest.setPaymentId(longDummy);
        this.paymentVerificationRequest.setVerified(boolDummy);
    }

    @Test
    void testCreate() {
        PaymentVerificationRequest newPaymentVerificationRequest = new PaymentVerificationRequest();
        newPaymentVerificationRequest.setPaymentId(paymentVerificationRequest.getPaymentId());
        newPaymentVerificationRequest.setVerified(paymentVerificationRequest.isVerified());

        assertEquals(paymentVerificationRequest.getPaymentId(), newPaymentVerificationRequest.getPaymentId());
        assertEquals(paymentVerificationRequest.isVerified(), newPaymentVerificationRequest.isVerified());
    }
}
