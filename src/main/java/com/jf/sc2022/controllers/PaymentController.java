package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.service.PaymentService;
import com.jf.sc2022.dto.payment.PaymentRequestDTO;
import com.jf.sc2022.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(@RequestBody final PaymentRequestDTO paymentRequestDTO) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequestDTO), HttpStatus.ACCEPTED);
    }
}
