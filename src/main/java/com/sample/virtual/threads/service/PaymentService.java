package com.sample.virtual.threads.service;

import com.sample.virtual.threads.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    @Retryable(
            retryFor = IOException.class,
            maxAttempts = 4,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    public PaymentResponse process() throws IOException {
        log.info("Trying payment...");
        // Simulate core/payment provider failure
        makePaymentInCoreLedger();
        return new PaymentResponse("SUCCESS");
    }

    @Recover
    public PaymentResponse recover(IOException ex) {

        log.info("Retries exhausted: {}", ex.getMessage());

        return new PaymentResponse("FAILED");
    }


    private void makePaymentInCoreLedger() throws IOException {
        throw new IOException("Payment provider unavailable");
    }
}