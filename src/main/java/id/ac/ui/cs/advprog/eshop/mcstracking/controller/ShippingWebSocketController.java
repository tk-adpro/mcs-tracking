package id.ac.ui.cs.advprog.eshop.mcstracking.controller;

import id.ac.ui.cs.advprog.eshop.mcstracking.model.Shipping;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ShippingWebSocketController {

    @MessageMapping("/shipping")
    @SendTo("/topic/shipping")
    public Shipping sendShipping(Shipping shipping) {
        return shipping;
    }
}

