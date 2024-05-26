package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        String dummy = "DUMMY";
        this.messageResponse = new MessageResponse();
        this.messageResponse.setMessage(dummy);
    }

    @Test
    void testCreate() {
        MessageResponse newMessageResponse = new MessageResponse();
        newMessageResponse.setMessage(messageResponse.getMessage());

        assertEquals(messageResponse.getMessage(), newMessageResponse.getMessage());
    }
}
