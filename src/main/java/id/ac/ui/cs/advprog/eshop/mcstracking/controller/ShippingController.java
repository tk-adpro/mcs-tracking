package id.ac.ui.cs.advprog.eshop.mcstracking.controller;

import id.ac.ui.cs.advprog.eshop.mcstracking.dto.GenericResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.MessageResponse;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.PaymentVerificationRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.dto.ShippingRequest;
import id.ac.ui.cs.advprog.eshop.mcstracking.service.ShippingService;
import id.ac.ui.cs.advprog.eshop.mcstracking.utils.ConstantUtils;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService){
        this.shippingService = shippingService;
    }

    public ResponseEntity<GenericResponse> verifyPayment(@RequestBody PaymentVerificationRequest request){
        try {
            GenericResponse response = shippingService.verifyPayment(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return serverErrorResponse();
        }
    }

    @PostMapping("/create-shipping")
    public ResponseEntity<GenericResponse> createShipping(@RequestBody ShippingRequest request){
        try {
            return new ResponseEntity<>(shippingService.createShipping(request), HttpStatus.OK);
        } catch (BadRequestException e){
            return badRequestResponse(e.getMessage());
        } catch (Exception e){
            return serverErrorResponse();
        }
    }

    public ResponseEntity<GenericResponse> getShippingByOrderId(@RequestParam Long orderId){
        try {
            return new ResponseEntity<>(shippingService.getShippingByOrderId(orderId), HttpStatus.OK);
        } catch (Exception e){
            return serverErrorResponse();
        }
    }

    public ResponseEntity<GenericResponse> getUserShipping(){
        try {
            return new ResponseEntity<>(shippingService.getUserShipping(), HttpStatus.OK);
        } catch (Exception e){
            return serverErrorResponse();
        }
    }

    public ResponseEntity<GenericResponse> updateShippingToDelivered(@RequestParam Long shippingId){
        try {
            return new ResponseEntity<>(shippingService.updateShippingToDelivered(shippingId), HttpStatus.OK);
        } catch (Exception e){
            return serverErrorResponse();
        }
    }

    private ResponseEntity<GenericResponse> badRequestResponse(String message){
        GenericResponse response = new GenericResponse();
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<GenericResponse> serverErrorResponse(){
        GenericResponse response = new GenericResponse();
        response.setMessage(ConstantUtils.INTERNAL_SERVER_ERROR_MSG);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
