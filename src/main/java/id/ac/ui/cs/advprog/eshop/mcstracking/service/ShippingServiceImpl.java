package id.ac.ui.cs.advprog.eshop.mcstracking.service;

import id.ac.ui.cs.advprog.eshop.mcstracking.config.AuthUtils;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.GenericResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.enums.ShippingStatus;
import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import id.ac.ui.cs.advprog.eshop.mcstracking.repository.ShippingRepository;
import id.ac.ui.cs.advprog.eshop.mcstracking.strategy.ShippingStrategy;
import id.ac.ui.cs.advprog.eshop.mcstracking.strategy.ShippingStrategyFactory;
import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public GenericResponse verifyPayment(PaymentVerificationRequest request) throws BadRequestException {
        GenericResponse response = new GenericResponse();
        String paymentStatus = shippingRepository.getPaymentStatus(request.getPaymentId());
        if (paymentStatus != null && paymentStatus.equals("PENDING")) {
            Long orderId = shippingRepository.getOrderIdByPayment(request.getPaymentId());
            if (request.isVerified()) {
                shippingRepository.updatePaymentValid(request.getPaymentId());
                response.setMessage("VALID");
                shippingRepository.updateOrderStatusInProgress(orderId);
            } else {
                shippingRepository.updatePaymentNotValid(request.getPaymentId());
                response.setMessage("NOT VALID");
                shippingRepository.updateOrderStatusCancelled(orderId);
            }
            response.setData(orderId);
            response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
            return response;
        } else throw new BadRequestException("Cannot verify this payment id");
    }

    @Override
    public GenericResponse createShipping(ShippingRequest request) throws BadRequestException {
        String orderStatus = shippingRepository.getOrderStatus(request.getOrderId());
        if (orderStatus != null && orderStatus.equals("IN_PROGRESS")) {
            if (shippingRepository.findByOrderId(request.getOrderId()) != null)
                throw new BadRequestException("Already assigned a shipping for this order");
            GenericResponse response = new GenericResponse();
            Shipping shipping = new Shipping();
            ShippingStrategy strategy = strategyFactory.getStrategy(request.getShippingMethod());
            if (strategy == null) throw new BadRequestException("Invalid shipping method");
            strategy.validateTrackingCode(request.getReceiptCode());
            shipping.setOrderId(request.getOrderId());
            shipping.setPaymentId(request.getPaymentId());
            shipping.setUserId(request.getUserId());
            shipping.setReceiptCode(request.getReceiptCode());
            shipping.setShippingMethod(request.getShippingMethod());
            shipping.setStatus(ShippingStatus.IN_PROGRESS.toString());
            shipping.setCreatedDate(new Date());
            response.setData(shippingRepository.save(shipping));
            response.setMessage("Shipping created");
            response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

            messagingTemplate.convertAndSend("/topic/shipping/" + shipping.getUserId(), shipping);
            return response;
        } else throw new BadRequestException("Cannot assign shipping to this order id");
    }

    public GenericResponse getUserShipping(){
        GenericResponse response = new GenericResponse();
        response.setData(shippingRepository.findByUserIdOrderByCreatedDateDesc(AuthUtils.getUserId()));
        response.setMessage("Get user's shippings successful");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    public GenericResponse updateShippingToDelivered(Long shippingId) throws BadRequestException {
        GenericResponse response = new GenericResponse();
        Shipping shipping = shippingRepository.findByIdAndUserId(shippingId, AuthUtils.getUserId());
        if (shipping == null) throw new BadRequestException("The specified shipping does not exist for this user");
        if (!shipping.getStatus().equals(ShippingStatus.IN_PROGRESS.toString())) throw new BadRequestException("Invalid status change");
        shipping.setStatus(ShippingStatus.DELIVERED.toString());
        shippingRepository.save(shipping);
        shippingRepository.updateOrderStatusDone(shipping.getOrderId());
        response.setData(shipping);
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        response.setMessage("Shipping status set to delivered");
        return response;
    }

    @Override
    public GenericResponse getUnverifiedPayments() {
        GenericResponse response = new GenericResponse();
        response.setData(shippingRepository.getUnverifiedPayments());
        response.setMessage("Payments successfully retrieved");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    @Override
    public GenericResponse getInProgressOrders() {
        GenericResponse response = new GenericResponse();
        response.setData(shippingRepository.getInProgressOrder());
        response.setMessage("Orders successfully retrieved");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    @Override
    public GenericResponse getShippingByOrderId(Long orderId) {
        GenericResponse response = new GenericResponse();
        response.setData(shippingRepository.findByOrderId(orderId));
        response.setMessage("Operation successful");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    @Override
    public GenericResponse getShippingByOrderIdAndUserId(Long orderId) throws BadRequestException {
        GenericResponse response = new GenericResponse();
        Shipping shipping = shippingRepository.findByOrderIdAndUserId(orderId, AuthUtils.getUserId());
        if (shipping == null) throw new BadRequestException("The specified shipping does not exist for this user");
        response.setMessage("Shipping found");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }
}
