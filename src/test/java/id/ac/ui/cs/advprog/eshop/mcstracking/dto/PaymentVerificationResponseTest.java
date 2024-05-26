package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentVerificationResponseTest {

    private PaymentVerificationResponse paymentVerificationResponse;

    @BeforeEach
    void setUp() {
        String dummy = "DUMMY";
        this.paymentVerificationResponse = new PaymentVerificationResponse();
        this.paymentVerificationResponse.setStatus(dummy);
        this.paymentVerificationResponse.setValue(dummy);
    }

    @Test
    void testCreate() {
        PaymentVerificationResponse newPaymentVerificationResponse = new PaymentVerificationResponse();
        newPaymentVerificationResponse.setStatus(paymentVerificationResponse.getStatus());
        newPaymentVerificationResponse.setValue(paymentVerificationResponse.getValue());

        assertEquals(paymentVerificationResponse.getStatus(), newPaymentVerificationResponse.getStatus());
        assertEquals(paymentVerificationResponse.getValue(), newPaymentVerificationResponse.getValue());
    }
}