package id.ac.ui.cs.advprog.eshop.mcstracking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShippingTest {

    private Shipping shipping;

    @BeforeEach
    void setUp(){
        Long longDummy = 1L;
        String dummy = "DUMMY";
        this.shipping = new Shipping();
        this.shipping.setId(longDummy);
        this.shipping.setUserId(longDummy);
        this.shipping.setStatus(dummy);
        this.shipping.setShippingMethod(dummy);
        this.shipping.setPaymentId(longDummy);
        this.shipping.setReceiptCode(dummy);
        this.shipping.setId(longDummy);
        this.shipping.setCreatedDate(new Date());
    }

    @Test
    void testCreate(){
        Shipping newShipping = new Shipping();
        newShipping.setId(shipping.getId());
        newShipping.setOrderId(shipping.getOrderId());
        newShipping.setPaymentId(shipping.getPaymentId());
        newShipping.setUserId(shipping.getUserId());
        newShipping.setStatus(shipping.getStatus());
        newShipping.setShippingMethod(shipping.getShippingMethod());
        newShipping.setReceiptCode(shipping.getReceiptCode());
        newShipping.setCreatedDate(shipping.getCreatedDate());

        assertArrayEquals(new Long[]{shipping.getId(), shipping.getOrderId(), shipping.getPaymentId(), shipping.getUserId()},
                new Long[]{newShipping.getId(), newShipping.getOrderId(), newShipping.getPaymentId(), newShipping.getUserId()});

        assertArrayEquals(new String[]{shipping.getShippingMethod(), shipping.getStatus(), shipping.getReceiptCode()},
                new String[]{newShipping.getShippingMethod(), newShipping.getStatus(), newShipping.getReceiptCode()});

        assertEquals(shipping.getCreatedDate(), newShipping.getCreatedDate());
    }
}
