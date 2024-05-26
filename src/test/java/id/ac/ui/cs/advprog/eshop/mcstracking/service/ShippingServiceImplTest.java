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
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ShippingServiceImplTest {

    @Mock
    private ShippingRepository shippingRepository;

    @Mock
    private ShippingStrategyFactory strategyFactory;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ShippingServiceImpl shippingService;

    private ShippingRequest shippingRequest;
    private PaymentVerificationRequest paymentVerificationRequest;
    private Shipping shipping;

    @BeforeEach
    void setUp() {
        shippingRequest = new ShippingRequest();
        shippingRequest.setOrderId(1L);
        shippingRequest.setPaymentId(1L);
        shippingRequest.setUserId(1L);
        shippingRequest.setReceiptCode("dummy");
        shippingRequest.setShippingMethod("STANDARD");

        paymentVerificationRequest = new PaymentVerificationRequest();
        paymentVerificationRequest.setPaymentId(1L);
        paymentVerificationRequest.setVerified(true);

        shipping = new Shipping();
        shipping.setOrderId(1L);
        shipping.setPaymentId(1L);
        shipping.setUserId(1L);
        shipping.setReceiptCode("dummy");
        shipping.setShippingMethod("STANDARD");
        shipping.setStatus(ShippingStatus.IN_PROGRESS.toString());
        shipping.setCreatedDate(new Date());
    }

    @Test
    void testVerifyPayment() throws BadRequestException {
        Mockito.when(shippingRepository.getPaymentStatus(paymentVerificationRequest.getPaymentId())).thenReturn("PENDING");
        Mockito.when(shippingRepository.getOrderIdByPayment(paymentVerificationRequest.getPaymentId())).thenReturn(1L);

        GenericResponse response = shippingService.verifyPayment(paymentVerificationRequest);

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("VALID", response.getMessage());
        assertEquals(1L, response.getData());

        Mockito.verify(shippingRepository).updatePaymentValid(paymentVerificationRequest.getPaymentId());
        Mockito.verify(shippingRepository).updateOrderStatusInProgress(1L);
    }

    @Test
    void testVerifyPaymentNotValid() throws BadRequestException {
        paymentVerificationRequest.setVerified(false);
        Mockito.when(shippingRepository.getPaymentStatus(paymentVerificationRequest.getPaymentId())).thenReturn("PENDING");
        Mockito.when(shippingRepository.getOrderIdByPayment(paymentVerificationRequest.getPaymentId())).thenReturn(1L);

        GenericResponse response = shippingService.verifyPayment(paymentVerificationRequest);

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("NOT VALID", response.getMessage());
        assertEquals(1L, response.getData());

        Mockito.verify(shippingRepository).updatePaymentNotValid(paymentVerificationRequest.getPaymentId());
        Mockito.verify(shippingRepository).updateOrderStatusCancelled(1L);
    }

    @Test
    void testVerifyPaymentFail() {
        Mockito.when(shippingRepository.getPaymentStatus(paymentVerificationRequest.getPaymentId())).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                shippingService.verifyPayment(paymentVerificationRequest));

        assertEquals("Cannot verify this payment id", exception.getMessage());

        Mockito.when(shippingRepository.getPaymentStatus(paymentVerificationRequest.getPaymentId())).thenReturn("VALID");

        exception = assertThrows(BadRequestException.class, () ->
                shippingService.verifyPayment(paymentVerificationRequest));

        assertEquals("Cannot verify this payment id", exception.getMessage());
    }

    @Test
    void testCreateShipping() throws BadRequestException {
        Mockito.when(shippingRepository.getOrderStatus(shippingRequest.getOrderId())).thenReturn("IN_PROGRESS");
        Mockito.when(shippingRepository.findByOrderId(shippingRequest.getOrderId())).thenReturn(null);
        ShippingStrategy strategy = Mockito.mock(ShippingStrategy.class);
        Mockito.when(strategyFactory.getStrategy(shippingRequest.getShippingMethod())).thenReturn(strategy);
        Mockito.when(shippingRepository.save(any(Shipping.class))).thenReturn(shipping);

        GenericResponse response = shippingService.createShipping(shippingRequest);

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("Shipping created", response.getMessage());
        assertEquals(shipping, response.getData());
    }

    @Test
    void testCreateShippingAlreadyAssigned() {
        Mockito.when(shippingRepository.getOrderStatus(shippingRequest.getOrderId())).thenReturn("IN_PROGRESS");
        Mockito.when(shippingRepository.findByOrderId(shippingRequest.getOrderId())).thenReturn(shipping);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                shippingService.createShipping(shippingRequest));

        assertEquals("Already assigned a shipping for this order", exception.getMessage());
    }

    @Test
    void testCreateShippingInvalidMethod() {
        Mockito.when(shippingRepository.getOrderStatus(shippingRequest.getOrderId())).thenReturn("IN_PROGRESS");
        Mockito.when(shippingRepository.findByOrderId(shippingRequest.getOrderId())).thenReturn(null);
        Mockito.when(strategyFactory.getStrategy(shippingRequest.getShippingMethod())).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                shippingService.createShipping(shippingRequest));

        assertEquals("Invalid shipping method", exception.getMessage());
    }

    @Test
    void testCreateShippingInvalidOrderStatus() {
        Mockito.when(shippingRepository.getOrderStatus(shippingRequest.getOrderId())).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                shippingService.createShipping(shippingRequest));

        assertEquals("Cannot assign shipping to this order id", exception.getMessage());

        Mockito.when(shippingRepository.getOrderStatus(shippingRequest.getOrderId())).thenReturn("CANCELLED");

        exception = assertThrows(BadRequestException.class, () ->
                shippingService.createShipping(shippingRequest));

        assertEquals("Cannot assign shipping to this order id", exception.getMessage());
    }

    @Test
    void testGetUserShipping() {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            Mockito.when(shippingRepository.findByUserIdOrderByCreatedDateDesc(1L)).thenReturn(List.of(shipping));

            GenericResponse response = shippingService.getUserShipping();

            assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
            assertEquals("Get user's shippings successful", response.getMessage());
            assertEquals(List.of(shipping), response.getData());
        }
    }

    @Test
    void testUpdateShippingToDelivered() throws BadRequestException {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            Mockito.when(shippingRepository.findByIdAndUserId(1L, 1L)).thenReturn(shipping);

            GenericResponse response = shippingService.updateShippingToDelivered(1L);

            assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
            assertEquals("Shipping status set to delivered", response.getMessage());
            assertEquals(shipping, response.getData());

            Mockito.verify(shippingRepository).save(shipping);
            Mockito.verify(shippingRepository).updateOrderStatusDone(shipping.getOrderId());
        }
    }

    @Test
    void testUpdateShippingToDeliveredNotExist() {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            Mockito.when(shippingRepository.findByIdAndUserId(1L, 1L)).thenReturn(null);

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    shippingService.updateShippingToDelivered(1L));

            assertEquals("The specified shipping does not exist for this user", exception.getMessage());
        }
    }

    @Test
    void testUpdateShippingToDeliveredInvalidStatus() {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            shipping.setStatus(ShippingStatus.DELIVERED.toString());
            Mockito.when(shippingRepository.findByIdAndUserId(1L, 1L)).thenReturn(shipping);

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    shippingService.updateShippingToDelivered(1L));

            assertEquals("Invalid status change", exception.getMessage());
        }
    }

    @Test
    void testGetUnverifiedPayments() {
        List<Object> unverifiedPayments = List.of(Map.of("paymentId", 1L));
        Mockito.when(shippingRepository.getUnverifiedPayments()).thenReturn(unverifiedPayments);

        GenericResponse response = shippingService.getUnverifiedPayments();

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("Payments successfully retrieved", response.getMessage());
        assertEquals(unverifiedPayments, response.getData());
    }

    @Test
    void testGetInProgressOrders() {
        List<Object> inProgressOrders = List.of(Map.of("orderId", 1L));
        Mockito.when(shippingRepository.getInProgressOrder()).thenReturn(inProgressOrders);

        GenericResponse response = shippingService.getInProgressOrders();

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("Orders successfully retrieved", response.getMessage());
        assertEquals(inProgressOrders, response.getData());
    }

    @Test
    void testGetShippingByOrderId() {
        Mockito.when(shippingRepository.findByOrderId(1L)).thenReturn(shipping);

        GenericResponse response = shippingService.getShippingByOrderId(1L);

        assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
        assertEquals("Operation successful", response.getMessage());
        assertEquals(shipping, response.getData());
    }

    @Test
    void testGetShippingByOrderIdAndUserId() throws BadRequestException {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            Mockito.when(shippingRepository.findByOrderIdAndUserId(1L, 1L)).thenReturn(shipping);

            GenericResponse response = shippingService.getShippingByOrderIdAndUserId(1L);

            assertEquals(ConstantUtils.RESPONSE_STATUS_SUCCESS, response.getStatus());
            assertEquals("Shipping found", response.getMessage());
        }
    }

    @Test
    void testGetShippingByOrderIdAndUserIdNotExist() {
        try (MockedStatic<AuthUtils> mockedAuthUtils = Mockito.mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUserId).thenReturn(1L);
            Mockito.when(shippingRepository.findByOrderIdAndUserId(1L, 1L)).thenReturn(null);

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    shippingService.getShippingByOrderIdAndUserId(1L));

            assertEquals("The specified shipping does not exist for this user", exception.getMessage());
        }
    }
}