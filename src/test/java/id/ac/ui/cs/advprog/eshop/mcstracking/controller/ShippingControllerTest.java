package id.ac.ui.cs.advprog.eshop.mcstracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.GenericResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.service.ShippingService;
import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import id.ac.ui.cs.advprog.eshop.mcstracking.dto.GenericResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.service.ShippingService;
import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShippingControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @MockBean
    private ShippingService shippingService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        this.mapper = new ObjectMapper();
    }

    @Test
    void testVerifyPayment_Success() throws Exception {
        PaymentVerificationRequest request = new PaymentVerificationRequest();
        GenericResponse response = new GenericResponse();
        response.setMessage("Payment verified");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        lenient().when(shippingService.verifyPayment(any(PaymentVerificationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/shipping/admin/verify-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).verifyPayment(any(PaymentVerificationRequest.class));
    }

    @Test
    void testVerifyPayment_BadRequest() throws Exception {
        lenient().when(shippingService.verifyPayment(any(PaymentVerificationRequest.class))).thenThrow(new BadRequestException("Bad request"));

        mockMvc.perform(post("/shipping/admin/verify-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new PaymentVerificationRequest()))) // replace with actual JSON body
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request"))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).verifyPayment(any(PaymentVerificationRequest.class));
    }

    @Test
    void testVerifyPayment_ServerError() throws Exception {
        lenient().when(shippingService.verifyPayment(any(PaymentVerificationRequest.class))).thenThrow(new NullPointerException("error"));

        mockMvc.perform(post("/shipping/admin/verify-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new PaymentVerificationRequest()))) // replace with actual JSON body
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).verifyPayment(any(PaymentVerificationRequest.class));
    }

    @Test
    void testCreateShipping_Success() throws Exception {
        ShippingRequest request = new ShippingRequest();
        GenericResponse response = new GenericResponse();
        response.setMessage("Shipping created");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        lenient().when(shippingService.createShipping(any(ShippingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/shipping/create-shipping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))) // replace with actual JSON body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Shipping created"))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).createShipping(any(ShippingRequest.class));
    }

    @Test
    void testCreateShipping_BadRequest() throws Exception {
        lenient().when(shippingService.createShipping(any(ShippingRequest.class))).thenThrow(new BadRequestException("Bad request"));

        mockMvc.perform(post("/shipping/create-shipping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new ShippingRequest()))) // replace with actual JSON body
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request"))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).createShipping(any(ShippingRequest.class));
    }

    @Test
    void testCreateShipping_ServerError() throws Exception {
        lenient().when(shippingService.createShipping(any(ShippingRequest.class))).thenThrow(new NullPointerException("error"));

        mockMvc.perform(post("/shipping/create-shipping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(new ShippingRequest()))) // replace with actual JSON body
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).createShipping(any(ShippingRequest.class));
    }

    @Test
    void testGetShippingByOrderId_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setMessage("Shipping details");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        lenient().when(shippingService.getShippingByOrderId(anyLong())).thenReturn(response);

        mockMvc.perform(get("/shipping/admin/get-shipping-by-order-id")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).getShippingByOrderId(anyLong());
    }

    @Test
    void testGetShippingByOrderIdAndUserId_BadRequestError() throws Exception {

        lenient().when(shippingService.getShippingByOrderIdAndUserId(anyLong())).thenThrow(new BadRequestException("error"));

        mockMvc.perform(get("/shipping/customer/get-shipping-by-order-id")
                        .param("orderId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getShippingByOrderIdAndUserId(anyLong());
    }

    @Test
    void testGetShippingByOrderId_ServerError() throws Exception {

        lenient().when(shippingService.getShippingByOrderId(anyLong())).thenThrow(new NullPointerException("error"));

        mockMvc.perform(get("/shipping/admin/get-shipping-by-order-id")
                        .param("orderId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getShippingByOrderId(anyLong());
    }

    @Test
    void testGetShippingByOrderIdAndUserId_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setMessage("Shipping details for user");
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        lenient().when(shippingService.getShippingByOrderIdAndUserId(anyLong())).thenReturn(response);

        mockMvc.perform(get("/shipping/customer/get-shipping-by-order-id")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).getShippingByOrderIdAndUserId(anyLong());
    }

    @Test
    void testGetShippingByOrderIdAndUserId_ServerError() throws Exception {
        GenericResponse response = new GenericResponse();

        lenient().when(shippingService.getShippingByOrderIdAndUserId(anyLong())).thenThrow(new NullPointerException("error"));

        mockMvc.perform(get("/shipping/customer/get-shipping-by-order-id")
                        .param("orderId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getShippingByOrderIdAndUserId(anyLong());
    }



    @Test
    void testGetUserShipping_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        when(shippingService.getUserShipping()).thenReturn(response);

        mockMvc.perform(get("/shipping/customer/get-shipping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).getUserShipping();
    }

    @Test
    void testGetUserShipping_ServerError() throws Exception {
        when(shippingService.getUserShipping()).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(get("/shipping/customer/get-shipping"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ConstantUtils.INTERNAL_SERVER_ERROR_MSG))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getUserShipping();
    }

    @Test
    void testUpdateShippingToDelivered_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        when(shippingService.updateShippingToDelivered(anyLong())).thenReturn(response);

        mockMvc.perform(post("/shipping/user/update-to-delivered")
                        .param("shippingId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).updateShippingToDelivered(anyLong());
    }

    @Test
    void testUpdateShippingToDelivered_BadRequest() throws Exception {
        when(shippingService.updateShippingToDelivered(anyLong())).thenThrow(new BadRequestException("Bad request"));

        mockMvc.perform(post("/shipping/user/update-to-delivered")
                        .param("shippingId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request"))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).updateShippingToDelivered(anyLong());
    }

    @Test
    void testUpdateShippingToDelivered_ServerError() throws Exception {
        when(shippingService.updateShippingToDelivered(anyLong())).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(post("/shipping/user/update-to-delivered")
                        .param("shippingId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ConstantUtils.INTERNAL_SERVER_ERROR_MSG));

        verify(shippingService, times(1)).updateShippingToDelivered(anyLong());
    }

    @Test
    void testGetUnverifiedPayments_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        when(shippingService.getUnverifiedPayments()).thenReturn(response);

        mockMvc.perform(get("/shipping/admin/get-unverified-payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).getUnverifiedPayments();
    }

    @Test
    void testGetUnverifiedPayments_ServerError() throws Exception {
        when(shippingService.getUnverifiedPayments()).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(get("/shipping/admin/get-unverified-payments"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ConstantUtils.INTERNAL_SERVER_ERROR_MSG))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getUnverifiedPayments();
    }

    @Test
    void testGetInProgressOrders_Success() throws Exception {
        GenericResponse response = new GenericResponse();
        response.setStatus(ConstantUtils.RESPONSE_STATUS_SUCCESS);

        when(shippingService.getInProgressOrders()).thenReturn(response);

        mockMvc.perform(get("/shipping/admin/get-orders-in-progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_SUCCESS));

        verify(shippingService, times(1)).getInProgressOrders();
    }

    @Test
    void testGetInProgressOrders_ServerError() throws Exception {
        when(shippingService.getInProgressOrders()).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(get("/shipping/admin/get-orders-in-progress"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ConstantUtils.INTERNAL_SERVER_ERROR_MSG))
                .andExpect(jsonPath("$.status").value(ConstantUtils.RESPONSE_STATUS_FAILED));

        verify(shippingService, times(1)).getInProgressOrders();
    }
}