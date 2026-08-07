package com.sample.virtual.threads.model;

import java.math.BigDecimal;

public record PaymentRequest(
        String id,
        BigDecimal amount
) {
}
