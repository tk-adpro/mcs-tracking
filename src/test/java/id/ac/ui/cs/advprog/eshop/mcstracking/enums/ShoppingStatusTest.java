package id.ac.ui.cs.advprog.eshop.mcstracking.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingStatusTest {

    @Test
    void testShippingFactory(){
        assertEquals("IN_PROGRESS", ShippingStatus.IN_PROGRESS.toString());
        assertEquals("DELIVERED", ShippingStatus.DELIVERED.toString());
    }
}
