package id.ac.ui.cs.advprog.eshop.mcstracking.service;

import id.ac.ui.cs.advprog.eshop.mcstracking.dto.MessageResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import id.ac.ui.cs.advprog.eshop.mcstracking.repository.ShippingRepository;
import id.ac.ui.cs.advprog.eshop.mcstracking.strategy.ShippingStrategy;
import id.ac.ui.cs.advprog.eshop.mcstracking.strategy.ShippingStrategyFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@Log4j2
public class ShippingServiceImpl implements ShippingService{

    private ShippingRepository shippingRepository;

    private ShippingStrategyFactory strategyFactory;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ShippingServiceImpl(ShippingRepository shippingRepository, ShippingStrategyFactory strategyFactory, SimpMessagingTemplate messagingTemplate){
        this.shippingRepository = shippingRepository;
        this.strategyFactory = strategyFactory;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public MessageResponse verifyPayment(PaymentVerificationRequest request) {
        MessageResponse response = new MessageResponse();
        Long orderId = shippingRepository.getOrderIdByPayment(request.getPaymentId());
        if (request.isVerified()){
            shippingRepository.updatePaymentValid(request.getPaymentId());
            response.setMessage("VALID");
            shippingRepository.updateOrderStatusInProgress(orderId);
        } else{
            shippingRepository.updatePaymentNotValid(request.getPaymentId());
            response.setMessage("NOT VALID");
            shippingRepository.updateOrderStatusCancelled(orderId);
        }
        //messagingTemplate.convertAndSendToUser();
        return response;
    }

    @Override
    public Shipping createShipping(ShippingRequest request) throws BadRequestException {
        Shipping shipping = new Shipping();
        ShippingStrategy strategy = strategyFactory.getStrategy(shipping.getShippingMethod());
        if (strategy == null) throw new BadRequestException("Invalid shipping method");
        if (request.getReceiptCode() == null) throw new BadRequestException("Receipt code should not be null");
        if (strategy.validateTrackingCode(request.getReceiptCode())){
            shipping.setOrderId(request.getOrderId());
            shipping.setPaymentId(request.getPaymentId());
            shipping.setReceiptCode(request.getReceiptCode());
            shipping.setShippingMethod(request.getShippingMethod());
        }
        return null;
    }

    @Override
    public Shipping getShippingByOrderId(Long orderId) {
        return shippingRepository.findByOrderId(orderId);
    }
}