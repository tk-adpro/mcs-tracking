package id.ac.ui.cs.advprog.eshop.mcstracking.service;

import id.ac.ui.cs.advprog.eshop.mcstracking.dto.MessageResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import org.apache.coyote.BadRequestException;

public interface ShippingService {
    public MessageResponse verifyPayment(PaymentVerificationRequest request);
    public Shipping createShipping(ShippingRequest request) throws BadRequestException;
    public Shipping getShippingByOrderId(Long orderId);


}
