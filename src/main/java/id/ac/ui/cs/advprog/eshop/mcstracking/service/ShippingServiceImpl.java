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
    public GenericResponse verifyPayment(PaymentVerificationRequest request) {
        GenericResponse response = new GenericResponse();
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
        response.setData(orderId);
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    @Override
    public GenericResponse createShipping(ShippingRequest request) throws BadRequestException {
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
        response.setData(shippingRepository.save(shipping));
        response.setMessage("Shipping created");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        messagingTemplate.convertAndSend("/topic/shipping/" + shipping.getUserId(), shipping);
        return response;
    }

    public GenericResponse getUserShipping(){
        GenericResponse response = new GenericResponse();
        response.setData(shippingRepository.findByUserIdOrderByCreatedDateDesc(AuthUtils.getUserId()));
        response.setMessage("Get user's shippings successful");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        return response;
    }

    public GenericResponse updateShippingToDelivered(Long shippingId){
        GenericResponse response = new GenericResponse();
        Shipping shipping = shippingRepository.findByIdAndUserId(shippingId, AuthUtils.getUserId());
        shipping.setStatus(ShippingStatus.DELIVERED.toString());
        shippingRepository.save(shipping);
        shippingRepository.updateOrderStatusDone(shipping.getOrderId());
        response.setData(shipping);
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);
        response.setMessage("Shipping status set to delivered");
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
}
