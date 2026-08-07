package com.sample.virtual.threads.controller;

import com.sample.virtual.threads.model.PaymentResponse;
import com.sample.virtual.threads.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PaymentRetryController {

    private final PaymentService paymentService;

    @PostMapping("/payments/retry")
    public PaymentResponse processPayment() throws IOException {
        return paymentService.process();
    }
}