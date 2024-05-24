package id.ac.ui.cs.advprog.eshop.mcstracking.service;

import id.ac.ui.cs.advprog.eshop.mcstracking.dto.GenericResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.MessageResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import org.apache.coyote.BadRequestException;

public interface ShippingService {
    public GenericResponse verifyPayment(PaymentVerificationRequest request);
    public GenericResponse createShipping(ShippingRequest request) throws BadRequestException;
    public GenericResponse getShippingByOrderId(Long orderId);
    public GenericResponse getUserShipping();
    public GenericResponse updateShippingToDelivered(Long shippingId);

}
